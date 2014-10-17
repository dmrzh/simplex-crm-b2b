package ru.dev_server.client.viewmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.Converter;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;
import ru.dev_server.client.AuthService;
import ru.dev_server.client.DynamicaColumnUtils;
import ru.dev_server.client.HibernateDaoImpl;
import ru.dev_server.client.I18nConverter;
import ru.dev_server.client.dao.ClientDAO;
import ru.dev_server.client.dao.ContactDAO;
import ru.dev_server.client.dao.JuridicalPersonDAO;
import ru.dev_server.client.dao.itevents.EventDAO;
import ru.dev_server.client.dao.itevents.ItGroupDAO;
import ru.dev_server.client.dao.itevents.ItNoteDAO;
import ru.dev_server.client.model.Client;
import ru.dev_server.client.model.Contact;
import ru.dev_server.client.model.ContactType;
import ru.dev_server.client.model.ItEvent;
import ru.dev_server.client.model.ItGroup;
import ru.dev_server.client.model.ItNote;
import ru.dev_server.client.model.JuridicalPerson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * .
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class JuridicalPersonVM  {
    private static final Logger LOG= LoggerFactory.getLogger(JuridicalPersonVM.class);


    @WireVariable
    private DynamicaColumnUtils dynamicaColumnUtils;

    @WireVariable("juridicalPersonDAO")
    private JuridicalPersonDAO juridicalPersonDAO;

    @WireVariable
    ClientDAO clientDAO;

    @WireVariable
    private ContactDAO contactDAO;

    @WireVariable
    private AuthService authService;

    @WireVariable
    private ItNoteDAO itNoteDAO;

    @WireVariable
    private EventDAO itEventDAO;

    @WireVariable
    private ItGroupDAO itGroupDAO;

    public boolean isDataDirty=false;


    private Window juridicalPersonWin;
    private JuridicalPerson currentJuridicalPerson;
    private Client selectedContactPerson;
    private List<Client> contactPersons=new ArrayList();
    private String fax;
    private String phones;

    private Contact selectedContact;

    private List<ItGroup> allPublicGroups;
    private List<ItGroup> selectedPublicGroups=new LinkedList<ItGroup>();
    private List<ItGroup> allPrivateGroups;
    private List<ItGroup> selectedPrivateGroups=new LinkedList<ItGroup>();
    private boolean disableEditPublic;




    private Converter typeConverter=new I18nConverter(ContactType.class);
    private Converter itEventTypeConverter=new I18nConverter(ItEvent.EventType.class);

    private ItEvent selectedItEvent;
    private List<ItEvent> itEvents=new ArrayList<ItEvent>();
    String juridicalPersonNames;



    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view,@ExecutionArgParam("juridicalPerson") JuridicalPerson juridicalPerson){
        if(juridicalPerson==null){
            throw new NullPointerException("juridicalPerson can't be null");
        }
        juridicalPersonWin=view;

        this.currentJuridicalPerson=juridicalPerson;
        if(juridicalPerson.getId()!=null){
            this.currentJuridicalPerson=juridicalPersonDAO.get(juridicalPerson.getId());
        }
        refresh();
        refreshGroups();
        refreshItEvents();
        refreshItNotes();
    }

    @Command
    public void dirty(){
        isDataDirty=true;
    }


    @GlobalCommand  @NotifyChange("itEvents")
    public void refreshItEvents(){
        if(currentJuridicalPerson.getId()!=null){
            itEvents=itEventDAO.findByJuridicalPerson(currentJuridicalPerson,0,1024);//todo show all
        }

    }
    @GlobalCommand   @NotifyChange({"allPublicGroups","selectedPublicGroups","allPrivateGroups","selectedPrivateGroups"})
     public void refreshGroups(){
         allPublicGroups=itGroupDAO.findPublic(authService.getCompany());
         selectedPublicGroups=currentJuridicalPerson.getPublicGroups();

        allPrivateGroups=itGroupDAO.findPrivate(authService.getEmployee());
        if(currentJuridicalPerson.getId()!=null){
            selectedPrivateGroups=itGroupDAO.findPrivate(authService.getEmployee(), currentJuridicalPerson) ;
        }


        ArrayList<GrantedAuthority> grantedAuthorities = HibernateDaoImpl.getGrantedAuthorities(authService.getEmployee());

        disableEditPublic=! grantedAuthorities.contains(HibernateDaoImpl.ROLE_LEADER);

     }



    @Command
    public void managePrivateGroups(){
        ItGroup group= new ItGroup();
        group.setOwner(authService.getEmployee());
        HashMap params = new HashMap();
        params.put("publicGroup",Boolean.FALSE);
        Executions.createComponents("/WEB-INF/zul/italtools/manageGroups.zul", null, params);
    }

    @Command
    public void managePublicGroups(){
        ItGroup group= new ItGroup();
        group.setOwner(authService.getEmployee());
        HashMap params = new HashMap();
        params.put("publicGroup",Boolean.TRUE);
        Executions.createComponents("/WEB-INF/zul/italtools/manageGroups.zul", null, params);
    }


    @GlobalCommand("refreshContacts")
    @NotifyChange({"currentJuridicalPerson"})
    public void refresh(){
        contactPersons=currentJuridicalPerson.getContactPerson();

        if( !isNewJuridicalPerson()){
            currentJuridicalPerson=juridicalPersonDAO.merge(currentJuridicalPerson);
            contactPersons=currentJuridicalPerson.getContactPerson();
        }
    }

    @GlobalCommand
    public void askSaveJuridicalPerson(){
        Messagebox.show("Клиент изменён!\n Cохранить перед выходом ?", "Внимание!", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, Messagebox.OK   ,new EventListener<Event>() {
            @Override
            public void onEvent(Event event) throws Exception {
                if ("onOK".equals(event.getName())) {
                    saveJuridicalPerson();
                } else {
                    juridicalPersonWin.detach();
                }
            }
        });
    }

    @Command
    public void cancelJuridicalPerson(){
        if(isDataDirty) {
            askSaveJuridicalPerson();
        }else{
            juridicalPersonWin.detach();
        }
    }

    @Command
    public void  saveJuridicalPerson(){

        currentJuridicalPerson.setCompany(authService.getCompany());



        juridicalPersonDAO.saveOrUpdate(currentJuridicalPerson);

        selectedPublicGroups=new LinkedList<ItGroup>(selectedPublicGroups);//todo bugfix clear groups
        currentJuridicalPerson.getPublicGroups().clear();
        currentJuridicalPerson.getPublicGroups().addAll(selectedPublicGroups);


        List<ItGroup> oldPrivate = new LinkedList<ItGroup>();
        if(currentJuridicalPerson.getId()!=null) {
            oldPrivate = itGroupDAO.findPrivate(authService.getEmployee(), currentJuridicalPerson);
        }


        selectedPrivateGroups=new LinkedList<ItGroup>(selectedPrivateGroups);//todo bugfix clear groups
        List<ItGroup> allPrivateGroups = currentJuridicalPerson.getPrivateGroups();
        allPrivateGroups.removeAll(oldPrivate);
        allPrivateGroups.addAll(selectedPrivateGroups);

        juridicalPersonDAO.saveOrUpdate(currentJuridicalPerson);

        juridicalPersonWin.detach();

        HashMap args = new HashMap();
        args.put("newJuridicalPerson",currentJuridicalPerson);
        BindUtils.postGlobalCommand(null, null, "refreshJuridicalPersons", args);

    }


    @Command
    public void createContact(){
        Contact contact = new Contact();
        contact.setClient(currentJuridicalPerson);
       // currentJuridicalPerson.getContacts().add(contact);
        HashMap params = new HashMap();
        params.put("contact",contact);

        Executions. createComponents("/contact.zul", null, params);

    }
    @Command @NotifyChange("currentJuridicalPerson")
    public void editContact(){
        HashMap params = new HashMap();
        params.put("contact",selectedContact);
        Executions. createComponents("/contact.zul", null, params);

    }
    @Command @NotifyChange({"selectedContact","currentJuridicalPerson"})
    public void deleteContact(){
        currentJuridicalPerson=juridicalPersonDAO.merge(currentJuridicalPerson);
       juridicalPersonDAO.saveOrUpdate(currentJuridicalPerson);
        selectedContact=null;
    }




    public boolean isNewJuridicalPerson(){
        return currentJuridicalPerson.getId()==null;
    }

    public JuridicalPerson getCurrentJuridicalPerson() {
        return currentJuridicalPerson;
    }

    public void setCurrentJuridicalPerson(JuridicalPerson currentJuridicalPerson) {
        this.currentJuridicalPerson = currentJuridicalPerson;
    }




    @Command
    public void createContactPerson(){
        if(!checkSave()){
            return;
        };
        HashMap params = new HashMap();
        Client client = dynamicaColumnUtils.createClient();
        client.setJuridicalPerson(currentJuridicalPerson);
        currentJuridicalPerson.getContactPerson().add(client);
        params.put("client", client);
        Executions. createComponents("/client.zul", null, params);
    }

    @Command   @NotifyChange({"currentJuridicalPerson","contactPersons"})
    public void deleteСontactPerson(){
        if(selectedContactPerson!=null) {
            currentJuridicalPerson=juridicalPersonDAO.merge(currentJuridicalPerson);
            selectedContactPerson=clientDAO.merge(selectedContactPerson);
            selectedContactPerson.setDeleted(true);
            selectedContactPerson.setJuridicalPerson(null);
            clientDAO.saveOrUpdate(selectedContactPerson);
            currentJuridicalPerson.getContactPerson().remove(selectedContactPerson);
            juridicalPersonDAO.saveOrUpdate(currentJuridicalPerson);
            contactPersons=currentJuridicalPerson.getContactPerson();
        }


    }

    @Command   @NotifyChange({"contactPersons"})
    public void editContactPerson(){
        if(selectedContactPerson==null){
            return;
        }
        HashMap params = new HashMap();
        Client client=selectedContactPerson;
        //if(selectedContactPerson.getId()!=null){
            client = clientDAO.get(selectedContactPerson.getId());
        ///}
        params.put("client", client);
        Executions. createComponents("/client.zul", null, params);
    }
    
    
    
    
    ////NOTE
    private ItNote selectedItNote;
    private List<ItNote> itNotes;

    public List<ItNote> getItNotes() {
        return itNotes;
    }

    public void setItNotes(List<ItNote> itNotes) {
        this.itNotes = itNotes;
    }

    public ItNote getSelectedItNote() {
        return selectedItNote;
    }

    public void setSelectedItNote(ItNote selectedItNote) {
        this.selectedItNote = selectedItNote;
    }
    @GlobalCommand  @NotifyChange("itNotes")
    public void refreshItNotes(){
        if(currentJuridicalPerson.getId()!=null){
            itNotes=itNoteDAO.findByJuridicalPerson(currentJuridicalPerson,authService.getEmployee());
        }
    }

    @Command @NotifyChange({"selectedItNote"})
    public void createNote(){
        if(!checkSave()){
            return;
        };

        ItNote itNote = new ItNote();
        itNote.setAuthor(authService.getEmployee());
        itNote.setJuridicalPerson(currentJuridicalPerson);
        HashMap params = new HashMap();
        params.put("itNote", itNote);
        selectedItNote=itNote;
        Executions. createComponents("/WEB-INF/zul/italtools/ItNote.zul", juridicalPersonWin, params);
    }

    @Command   @NotifyChange({"itNotes","selectedItNote"})
    public void deleteNote(){
        if(selectedItNote==null){
            return;
        }
        selectedItNote = itNoteDAO.get(selectedItNote.getId());
        itNoteDAO.delete(selectedItNote);
        selectedItNote=null;
        refreshItNotes();
    }

    @Command
    public void editItNote(){
        if(selectedItNote==null){
            return;
        }
        HashMap params = new HashMap();
        selectedItNote = itNoteDAO.get(selectedItNote.getId());
        params.put("itNote", selectedItNote);
        Executions. createComponents("/WEB-INF/zul/italtools/ItNote.zul", juridicalPersonWin, params);
    }
    

    @GlobalCommand    @NotifyChange({"contactPersons"})
    public  void refreshClients() {
        contactPersons=clientDAO.findContactPersonsJuridicalPerson(currentJuridicalPerson);
    }


    @Command
    public void createBill(){
        if(!checkSave()){
            return;
        };
        ItEvent itEvent = new ItEvent();
        itEvent.setOwner(authService.getEmployee());
        itEvent.setClient(currentJuridicalPerson);
        itEvent.setEventType(ItEvent.EventType.BILL);
        HashMap params = new HashMap();
        params.put("itEvent", itEvent);
        Executions. createComponents("/WEB-INF/zul/italtools/bill.zul", null, params);
    }

    /**
     * return true if possible add child object (if currentJuridicalPerson not new)
     * @return
     */
    private boolean checkSave() {
        if(currentJuridicalPerson.getId()==null){
            Messagebox.show("Перед добавлением, сохраните клиента.", "Внимание!",Messagebox.OK,Messagebox.EXCLAMATION);
        }
        return currentJuridicalPerson.getId()!=null;
    }

    @Command
    public void createMeeting(){
        if(!checkSave()){
            return;
        };
        ItEvent itEvent = new ItEvent();
        itEvent.setOwner(authService.getEmployee());
        itEvent.setClient(currentJuridicalPerson);
        itEvent.setEventType(ItEvent.EventType.MEETING);
        HashMap params = new HashMap();
        params.put("itEvent", itEvent);
        Executions. createComponents("/WEB-INF/zul/italtools/meetingOrCall.zul", null, params);
    }
    @Command
    public void createCall(){
        if(!checkSave()){
            return;
        };
        ItEvent itEvent = new ItEvent();
        itEvent.setOwner(authService.getEmployee());
        itEvent.setClient(currentJuridicalPerson);
        itEvent.setEventType(ItEvent.EventType.CALL);
        HashMap params = new HashMap();
        params.put("itEvent", itEvent);
        Executions. createComponents("/WEB-INF/zul/italtools/meetingOrCall.zul", null, params);
    }
    @Command
    public void editItMeeting(){

        HashMap params = new HashMap();
        params.put("itEvent", selectedItEvent);
        if(selectedItEvent.getEventType()== ItEvent.EventType.BILL){
            Executions. createComponents("/WEB-INF/zul/italtools/bill.zul", null, params);
        }else{
            Executions. createComponents("/WEB-INF/zul/italtools/meetingOrCall.zul", null, params);
        }
    }
    @Command @NotifyChange({"itEvents","selectedItEvent"})
    public void deleteItMeeting(){
        if(selectedItEvent!=null){
            ArrayList<GrantedAuthority> grantedAuthorities = HibernateDaoImpl.getGrantedAuthorities(authService.getEmployee());
            boolean canDaleteMeeting= grantedAuthorities.contains(HibernateDaoImpl.ROLE_LEADER);
            if(!canDaleteMeeting){
                Messagebox.show("Нет прав на удаление заметки", "Внимание!", Messagebox.OK , Messagebox.ERROR);
                return;
            }
            if (selectedItEvent.getId()!=null){
                itEventDAO.delete(itEventDAO.merge(selectedItEvent));
            }
        }
        selectedItEvent=null;
        refreshItEvents();
    }


    public String concat(String s1,String s2){
        return s1+s2;
    }
    public String trunc(String s1, int length){
        if(s1.length()>length&&length<=2){
            return s1.substring(0,length);
        }
        if(s1.length()>length){
            return s1.substring(0,length-2)+"..";
        }
        return s1;
    }


    public Client getSelectedContactPerson() {
        return selectedContactPerson;
    }

    public void setSelectedContactPerson(Client selectedContactPerson) {
        this.selectedContactPerson = selectedContactPerson;
    }

    public Converter getTypeConverter() {
        return typeConverter;
    }

    public Converter getItEventTypeConverter() {
        return itEventTypeConverter;
    }

    public Contact getSelectedContact() {
        return selectedContact;
    }

    public void setSelectedContact(Contact selectedContact) {
        this.selectedContact = selectedContact;
    }

    public List<ItEvent> getItEvents() {
        return itEvents;
    }

    public void setItEvents(List<ItEvent> itEvents) {
        this.itEvents = itEvents;
    }

    public List<Client> getContactPersons() {
        return contactPersons;
    }

    public void setContactPersons(List<Client> contactPersons) {
        this.contactPersons = contactPersons;
    }

    public List<ItGroup> getSelectedPublicGroups() {
        return selectedPublicGroups;
    }

    public void setSelectedPublicGroups(List<ItGroup> selectedPublicGroups) {
        this.selectedPublicGroups = selectedPublicGroups;
    }

    public List<ItGroup> getAllPublicGroups() {
        return allPublicGroups;
    }

    public void setAllPublicGroups(List<ItGroup> allPublicGroups) {
        this.allPublicGroups = allPublicGroups;
    }

    public List<ItGroup> getAllPrivateGroups() {
        return allPrivateGroups;
    }

    public void setAllPrivateGroups(List<ItGroup> allPrivateGroups) {
        this.allPrivateGroups = allPrivateGroups;
    }

    public List<ItGroup> getSelectedPrivateGroups() {
        return selectedPrivateGroups;
    }

    public void setSelectedPrivateGroups(List<ItGroup> selectedPrivateGroups) {
        this.selectedPrivateGroups = selectedPrivateGroups;
    }

    public boolean isDisableEditPublic() {
        return disableEditPublic;
    }


    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getPhones() {
        return phones;
    }

    public void setPhones(String phones) {
        this.phones = phones;
    }

    public ItEvent getSelectedItEvent() {
        return selectedItEvent;
    }

    public void setSelectedItEvent(ItEvent selectedItEvent) {
        this.selectedItEvent = selectedItEvent;
    }
}
