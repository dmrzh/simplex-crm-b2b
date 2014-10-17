package ru.dev_server.client.viewmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.Converter;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;
import ru.dev_server.client.AuthService;
import ru.dev_server.client.DynamicaColumnUtils;
import ru.dev_server.client.ExcelUtils;
import ru.dev_server.client.HibernateDaoImpl;
import ru.dev_server.client.I18nConverter;
import ru.dev_server.client.dao.CategoryDAO;
import ru.dev_server.client.dao.DynamicColumnDAO;
import ru.dev_server.client.dao.JuridicalOldFilterDAO;
import ru.dev_server.client.dao.JuridicalPersonDAO;
import ru.dev_server.client.dao.MeetingDAO;
import ru.dev_server.client.dao.itevents.ItGroupDAO;
import ru.dev_server.client.model.ContactType;
import ru.dev_server.client.model.JuridicalPerson;
import ru.dev_server.client.model.JuridicalPersonFilter;

import java.util.HashMap;

/**.*/
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class JuridicalPersonListViewModel {
    private static final Logger LOG=LoggerFactory.getLogger(JuridicalPersonListViewModel.class) ;



    @WireVariable
    private JuridicalPersonDAO juridicalPersonDAO;
    @WireVariable("authService")
    private AuthService authService;
    @WireVariable("categoryDAO")
    private CategoryDAO categoryDAO;

    @WireVariable
    private MeetingDAO meetingDAO;

    @WireVariable
    private ItGroupDAO itGroupDAO;

    @WireVariable
    private DynamicColumnDAO dynamicColumnDAO;

    @WireVariable
    private ExcelUtils excelUtils;

    @WireVariable
    private DynamicaColumnUtils dynamicaColumnUtils;

    @WireVariable
    private JuridicalOldFilterDAO juridicalOldFilterDAO;


    private JuridicalPerson selectedItem;

    private Converter typeConverter=new I18nConverter(ContactType.class);

    private JuridicalPersonListModel juridicalPersonList;

    public boolean adminRole;


    private Window clientsListWin;

    @Init
    public void initFoo(){
        juridicalPersonList=new JuridicalPersonListModel(juridicalPersonDAO, authService, itGroupDAO,juridicalOldFilterDAO);
        adminRole= HibernateDaoImpl.getGrantedAuthorities(authService.getEmployee()).contains(HibernateDaoImpl.ROLE_ADMIN);
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view){
        clientsListWin=view;
    }


    /**
     * @param newJuridicalPerson if new JuridicalPerson created - new JuridicalPerson, else null.
     */
    @GlobalCommand  @NotifyChange({"clientList","selectedItem"})
    public  void refreshJuridicalPersons(@BindingParam("newJuridicalPerson")JuridicalPerson newJuridicalPerson) {
        juridicalPersonList.clearData();
        if(newJuridicalPerson!=null){
            selectedItem=newJuridicalPerson;
            juridicalPersonList.selectPerson(newJuridicalPerson);

        }
    }


    @Command("newClient")
    public void newClient(){
        HashMap params = new HashMap();
        params.put("client",dynamicaColumnUtils.createClient());
        Executions. createComponents("/client.zul", null, params);
    }


    @Command("newJuridicalPerson")
    public void newJuridicalPerson(){
        HashMap params = new HashMap();
        JuridicalPerson juridicalPerson= new JuridicalPerson();
        juridicalPerson.setCompany(authService.getCompany());
        params.put("juridicalPerson", juridicalPerson);
        Executions. createComponents("/WEB-INF/zul/juridicalPerson.zul", null, params);
    }


    @GlobalCommand("editJuridicalPerson")
    public void editClient(@BindingParam("juridicalPerson")JuridicalPerson juridicalPerson){
        if(juridicalPerson==null){
            if(selectedItem==null){
                return;
            }
        }
        HashMap params = new HashMap();
        params.put("juridicalPerson",juridicalPerson!=null?juridicalPerson:selectedItem);
        Executions. createComponents("/WEB-INF/zul/juridicalPerson.zul", null, params);
    }
    @Command
    public void  openFilter(){
        HashMap params = new HashMap();
        JuridicalPersonFilter filter = juridicalPersonList.getFilter();
        if(filter==null){
            filter= new JuridicalPersonFilter();
        }
        params.put("filter", filter);
        Executions.createComponents("WEB-INF/zul/juridicalPersonFilter.zul", null, params);
    }

    @Command @NotifyChange({"juridicalPersonList"})
    public void clearFilter(){
        juridicalPersonList.setFilter(null);
        refreshJuridicalPersons(null);
    }


    @GlobalCommand @NotifyChange({"juridicalPersonList"})
    public void filterJuridicalPersons(@BindingParam("filter") JuridicalPersonFilter filter){
        juridicalPersonList.setFilter(filter);
        refreshJuridicalPersons(null);
    }

    @Command("deleteJuridicalPerson") @NotifyChange({"juridicalPersonList","selectedItem"})
    public void deleteJuridicalPerson(){
        HashMap params = new HashMap();
        params.put("juridicalPerson", selectedItem);
        Executions. createComponents("/WEB-INF/zul/confirmDeleteJuridicalPerson.zul", null, params);

    }


    public JuridicalPersonListModel getClientList() {
        return juridicalPersonList;
    }


    public Converter getTypeConverter() {
        return typeConverter;
    }


    public JuridicalPerson getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(JuridicalPerson selectedItem) {
        this.selectedItem = selectedItem;
    }

    public boolean isAdminRole() {
        return adminRole;
    }


}
