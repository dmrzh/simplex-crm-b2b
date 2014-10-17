package ru.dev_server.client.viewmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.Converter;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.converter.FormatedDateConverter;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import ru.dev_server.client.AuthService;
import ru.dev_server.client.I18nConverter;
import ru.dev_server.client.dao.CategoryDAO;
import ru.dev_server.client.dao.ClientDAO;
import ru.dev_server.client.dao.ContactDAO;
import ru.dev_server.client.dao.DynamicColumnDAO;
import ru.dev_server.client.dao.JuridicalPersonDAO;
import ru.dev_server.client.model.Category;
import ru.dev_server.client.model.Client;
import ru.dev_server.client.model.Contact;
import ru.dev_server.client.model.ContactType;
import ru.dev_server.client.model.DynamicColumn;
import ru.dev_server.client.model.ExstendedTables;
import ru.dev_server.client.model.JuridicalPerson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**.*/
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ClientVM {
    private static final Logger LOG= LoggerFactory.getLogger(ClientVM.class);

    @WireVariable("clientDAO")
    private ClientDAO clientDAO;
    @WireVariable("categoryDAO")
    private CategoryDAO categoryDAO;
    @WireVariable("contactDAO")
    private ContactDAO contactDAO;
    @WireVariable("authService")
    private AuthService authService;
    @WireVariable
    private JuridicalPersonDAO juridicalPersonDAO;
    @WireVariable
    DynamicColumnDAO dynamicColumnDAO;


    private Window clientWin;

    private List<Category> categories;

    private Contact selectedContact;
    private ContactType[] contactTypes= ContactType.values();


    private Client currentClient;

    private List<DynamicColumn> dynamicColumnList=new ArrayList<DynamicColumn>();

    private FormatedDateConverter formatedDateConverter = new FormatedDateConverter();
    private Converter typeConverter=new I18nConverter(ContactType.class);


    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view,@ExecutionArgParam("client") Client client){
        if(client==null){
            throw new NullPointerException("client can't be null");
        }
        clientWin=view;
        currentClient=client;
        refresh();

    }

    @Command
    public void saveClient(){
        JuridicalPerson juridicalPerson = currentClient.getJuridicalPerson();
        if(juridicalPerson !=null&& juridicalPerson.getId()==null){
            juridicalPersonDAO.saveOrUpdate(juridicalPerson);
        }
        currentClient.setCompany(authService.getCompany());
        clientDAO.merge(currentClient);
        clientWin.detach();
    }

    @Command
    public void cancelClient(){
        clientWin.detach();
    }


    boolean isMeetingHistoryTabOpen=false;
    @Command()
    public void openMeetingHistory(){
        if(!isMeetingHistoryTabOpen){
            Component meetingHistoryPanel =clientWin.getFellow("meetingHistoryTabPanel");
            HashMap params = new HashMap();
            params.put("client",currentClient);
            Executions.createComponents("/meetingHistoryInc.zul", meetingHistoryPanel , params);
            isMeetingHistoryTabOpen=true;
        }

    }
    boolean isSmsHistoryTabOpen=false;
    @Command()
    public void openSmsHistory(){
        if(!isSmsHistoryTabOpen){
            Component smsHistoryTabPanel =clientWin.getFellow("smsHistoryTabPanel");
            HashMap params = new HashMap();
            params.put("client",currentClient);
            Executions.createComponents("/WEB-INF/zul/smsHistory.zul", smsHistoryTabPanel , params);
            isSmsHistoryTabOpen=true;
        }
    }


    @Command
    public void createContact(){
        Contact contact = new Contact();
        contact.setClient(currentClient);
        HashMap params = new HashMap();
        params.put("contact",contact);

        Executions. createComponents("/contact.zul", null, params);

    }
    @Command @NotifyChange("currentClient")
    public void editContact(){
        if(selectedContact==null){
            return;
        }

        HashMap params = new HashMap();
        params.put("contact",selectedContact);

        Executions. createComponents("/contact.zul", null, params);

    }
    @Command @NotifyChange({"selectedContact","currentClient"})
    public void deleteContact(){
        if(selectedContact==null){
            return;
        }
        currentClient=clientDAO.merge(currentClient);
        currentClient.getContacts().remove(selectedContact);
        clientDAO.saveOrUpdate(currentClient);
        selectedContact=null;
    }

    @Command @NotifyChange({"currentClient"})
    public void upContact(){
        //currentClient=clientDAO.get(currentClient.getId());
        List<Contact> contacts = currentClient.getContacts();

        int i = contacts.indexOf(selectedContact);
        if(i==0){
            Messagebox.show("Контакт уже самый первый");
            return;
        }
        contacts.set(i,contacts.get(i-1));
        contacts.set(i-1,selectedContact);
    }
    @Command @NotifyChange({"currentClient"})
    public void downContact(){
        currentClient=clientDAO.get(currentClient.getId());
        List<Contact> contacts = currentClient.getContacts();
        int i = contacts.indexOf(selectedContact);
        if(i==contacts.size()-1){
            Messagebox.show("Контакт уже самый последний");
            return;
        }
        contacts.set(i,contacts.get(i+1));
        contacts.set(i+1,selectedContact);
    }

    @GlobalCommand("refreshContacts")
    @NotifyChange({"currentClient"})
    public void refresh(){
        categories=categoryDAO.findAll(authService.getCompany());
        dynamicColumnList=dynamicColumnDAO.findAll(authService.getCompany(), ExstendedTables.CLIENT);
        if( !isNewClient()){
            currentClient=clientDAO.merge(currentClient);
        }

    }
    public boolean isNewClient(){
        return currentClient.getId()==null;
    }


    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Contact getSelectedContact() {
        return selectedContact;
    }

    public void setSelectedContact(Contact selectedContact) {
        this.selectedContact = selectedContact;
    }

    public ContactType[] getContactTypes() {
        return contactTypes;
    }

    public void setContactTypes(ContactType[] contactTypes) {
        this.contactTypes = contactTypes;
    }

    public Client getCurrentClient() {
        return currentClient;
    }

    public void setCurrentClient(Client currentClient) {
        this.currentClient = currentClient;
    }


    public List<DynamicColumn> getDynamicColumnList() {
        return dynamicColumnList;
    }

    public void setDynamicColumnList(List<DynamicColumn> dynamicColumnList) {
        this.dynamicColumnList = dynamicColumnList;
    }


    public FormatedDateConverter getFormatedDateConverter() {
        return formatedDateConverter;
    }

    public Converter getTypeConverter() {
        return typeConverter;
    }
}
