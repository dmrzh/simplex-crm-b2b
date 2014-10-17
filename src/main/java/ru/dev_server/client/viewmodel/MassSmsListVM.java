package ru.dev_server.client.viewmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import ru.dev_server.client.AuthService;
import ru.dev_server.client.dao.ClientDAO;
import ru.dev_server.client.dao.MassSmsDAO;
import ru.dev_server.client.model.MassSms;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**.*/
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class MassSmsListVM {
    private static final Logger LOG= LoggerFactory.getLogger(MassSmsListVM.class);

    List<Object[]> massSmsList;
    @WireVariable
    private MassSmsDAO massSmsDAO;
    @WireVariable
    private ClientDAO clientDAO;
    @WireVariable
    private AuthService authService;
    private  Object[] selectedMassSms;
    private Window smsSpamListWin;


    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view){
        smsSpamListWin=view;
        massSmsList=massSmsDAO.findAll(authService.getCompany(),0,10);
    }

    @GlobalCommand  @NotifyChange("massSmsList")
    public void refreshMassSmsList(){
        massSmsList=massSmsDAO.findAll(authService.getCompany(),0,10);
    }

    @Command
    public void delete(){
        if(selectedMassSms==null || selectedMassSms.length==0){
            Messagebox.show("Выберите рассылку для удаления","Внимание!",Messagebox.OK,Messagebox.EXCLAMATION);
            return;
        }
        MassSms massSms = (MassSms) selectedMassSms[0];
        massSms=massSmsDAO.get(massSms.getId());
        massSms.setDeleted(true);
        massSmsDAO.saveOrUpdate(massSms);
    }
    @Command  @NotifyChange("massSmsList")
    public void start(){
        MassSms massSms = (MassSms) selectedMassSms[0];
        massSms=massSmsDAO.get(massSms.getId());
        massSms.setActive(true);
        massSmsDAO.saveOrUpdate(massSms);
        refreshMassSmsList();
    }

    @Command @NotifyChange("massSmsList")
    public void stop(){
        MassSms massSms = (MassSms) selectedMassSms[0];
        massSms=massSmsDAO.get(massSms.getId());
        massSms.setActive(false);
        massSmsDAO.saveOrUpdate(massSms);
        refreshMassSmsList();
    }

    @Command
    public void create(){
        MassSms massSms = new MassSms();
        massSms.setName("");
        massSms.setCompany(authService.getCompany());
        massSms.setStartDate(new Date());
        HashMap params = new HashMap();
        params.put("massSms",massSms);
        Executions. createComponents("/WEB-INF/zul/massSms.zul", null, params);
    }

    @Command
    public void edit(){
        if(selectedMassSms==null || selectedMassSms.length==0){
            Messagebox.show("Выберите рассылку для редактирования","Внимание!",Messagebox.OK,Messagebox.EXCLAMATION);
            return;
        }
        MassSms massSms = (MassSms) selectedMassSms[0];
        massSms=massSmsDAO.get(massSms.getId());
        HashMap params = new HashMap();
        params.put("massSms",massSms);
        Executions. createComponents("/WEB-INF/zul/massSms.zul", null, params);
    }

    public List<Object[]> getMassSmsList() {
        return massSmsList;
    }

    public Object[] getSelectedMassSms() {
        return selectedMassSms;
    }

    public void setSelectedMassSms(Object[] selectedMassSms) {
        this.selectedMassSms = selectedMassSms;
    }
}
