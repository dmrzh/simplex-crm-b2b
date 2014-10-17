package ru.dev_server.client.viewmodel.italtools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import ru.dev_server.client.AuthService;
import ru.dev_server.client.dao.JuridicalPersonDAO;
import ru.dev_server.client.dao.itevents.ItGroupDAO;
import ru.dev_server.client.model.ItGroup;
import ru.dev_server.client.model.JuridicalPerson;

import java.util.HashMap;
import java.util.List;

/**
 * .
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ManageGroupsVM {
    private static final Logger LOG= LoggerFactory.getLogger(ManageGroupsVM.class);


    @WireVariable
    private ItGroupDAO itGroupDAO;
    @WireVariable
    private JuridicalPersonDAO juridicalPersonDAO;

    @WireVariable
    private AuthService authService;

    private List<ItGroup> groups;

    private ItGroup selectedGroup;

    private Window itManageGroupWin;

    private Boolean publicGroup=false;


    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view,@ExecutionArgParam("publicGroup") Boolean publicGroup){
        this.itManageGroupWin=view;
        this.publicGroup=publicGroup;
        refreshGroups();
    }

    @GlobalCommand
    @NotifyChange("groups")
    public void refreshGroups() {
        if(publicGroup){
            groups=itGroupDAO.findPublic(authService.getCompany());
        }else{
            groups=itGroupDAO.findPrivate(authService.getEmployee());
        }
    }

    @Command @NotifyChange("groups")
    public void deleteGroup(){
        Messagebox.show("Удалить группу "+selectedGroup.getName() +"?", "Удаление",Messagebox.YES|Messagebox.NO,Messagebox.EXCLAMATION, new EventListener<Event>() {
            @Override
            public void onEvent(Event event) throws Exception {
                if(! "onYes".equals(event.getName())){
                    return;
                }
                selectedGroup=itGroupDAO.get(selectedGroup.getId());
                List<JuridicalPerson> byGroup = juridicalPersonDAO.findByGroup(authService.getCompany(), selectedGroup);
                for(JuridicalPerson jp:byGroup){       //todo chancge to annotation
                    jp.getPublicGroups().remove(selectedGroup);
                    jp.getPrivateGroups().remove(selectedGroup);
                    juridicalPersonDAO.saveOrUpdate(jp);
                }
                groups.remove(selectedGroup);
                itGroupDAO.delete(selectedGroup);


                HashMap<String, Object> args = new HashMap<String, Object>();
                BindUtils.postGlobalCommand(null, null, "refreshGroups", args);
            }
        });

    }

    @Command @NotifyChange("groups")
    public void addGroup(){
        ItGroup group= new ItGroup();
        if(publicGroup){
            group.setCompany(authService.getCompany());
        }else{
            group.setOwner(authService.getEmployee());
        }
        HashMap params = new HashMap();
        params.put("group",group);
        Executions.createComponents("/WEB-INF/zul/italtools/group.zul", null, params);
    }
    @Command @NotifyChange("groups")
    public void editGroup(){
        HashMap params = new HashMap();
        params.put("group",selectedGroup);
        Executions.createComponents("/WEB-INF/zul/italtools/group.zul", null, params);
    }


    public List<ItGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<ItGroup> groups) {
        this.groups = groups;
    }

    public ItGroup getSelectedGroup() {
        return selectedGroup;
    }

    public void setSelectedGroup(ItGroup selectedGroup) {
        this.selectedGroup = selectedGroup;
    }
}
