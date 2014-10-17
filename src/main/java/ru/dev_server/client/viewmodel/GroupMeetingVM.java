package ru.dev_server.client.viewmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.Converter;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import ru.dev_server.client.AuthService;
import ru.dev_server.client.DynamicaColumnUtils;
import ru.dev_server.client.I18nConverter;
import ru.dev_server.client.dao.ClientDAO;
import ru.dev_server.client.dao.DynamicColumnDAO;
import ru.dev_server.client.dao.MeetingDAO;
import ru.dev_server.client.dao.SmsNotificationDAO;
import ru.dev_server.client.model.Client;
import ru.dev_server.client.model.Meeting;
import ru.dev_server.client.model.NotificationStatus;

import java.util.HashMap;
import java.util.List;

/**. */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class GroupMeetingVM {
    private static final Logger LOG= LoggerFactory.getLogger(GroupMeetingVM.class);

    private Meeting currentMeeting;
    private Client currentClient;

    private Client clientToAdd;

    @WireVariable("clientDAO")
    private ClientDAO clientDAO;
    @WireVariable("meetingDAO")
    private MeetingDAO meetingDAO;
    @WireVariable("authService")
    private AuthService authService;
    @WireVariable
    DynamicColumnDAO dynamicColumnDAO;
    @WireVariable
    SmsNotificationDAO smsNotificationDAO;

    @WireVariable
    private DynamicaColumnUtils dynamicaColumnUtils;

    private Converter statusConverter =new I18nConverter(NotificationStatus.class);

    @AfterCompose
    public void afterCompose(@ExecutionArgParam("meeting") Meeting meeting){
        currentMeeting=meetingDAO.get(meeting.getId());
        filterAllClients();
    }

    private List<Client> filteredClients;
    private String filter="";



    public Meeting getCurrentMeeting() {
        return currentMeeting;
    }

    @GlobalCommand
    @NotifyChange({"filteredClients","currentMeeting"})
    public void refreshClients(){
        filterAllClients();
    }

    @Command
    @NotifyChange({"currentMeeting"})
    public void newClient(){
        Client client = dynamicaColumnUtils.createClient();
        //currentMeeting.addClient(client); todo
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("client",client);
        BindUtils.postGlobalCommand(null, null, "editClient", args);

    }

    @Command
    @NotifyChange({"clientToAdd","currentMeeting"})
    public void addClient(){
        LOG.debug("add client");
        if(clientToAdd==null){
            return;
        }
        clientToAdd=clientDAO.get(clientToAdd.getId());
        currentMeeting=meetingDAO.get(currentMeeting.getId());
        currentMeeting.addClient(clientToAdd);
        meetingDAO.saveOrUpdate(currentMeeting);
        clientToAdd=null;
    }
    @Command
    public void saveNote(){
        meetingDAO.saveOrUpdate(currentMeeting);
    }

    @Command
    @NotifyChange({"currentMeeting"})
    public void delete(){
        if(currentClient==null){
            return;
        }
        if((!currentClient.equals(currentMeeting.getClient()))){
            currentMeeting.getNotifications().remove(currentClient);
        }
        currentMeeting.getClientList().remove(currentClient);
        meetingDAO.saveOrUpdate(currentMeeting);
    }

    @Command()
    @NotifyChange("filteredClients")
    public void filterAllClients(){
        String query = new ClientUtils().checkFilterAndReturnQuery(filter);
        filteredClients=clientDAO.findByFilter(query,authService.getCompany());
    }

    public Client getClientToAdd() {
        return clientToAdd;
    }

    public void setClientToAdd(Client clientToAdd) {
        this.clientToAdd = clientToAdd;
    }

    public Client getCurrentClient() {
        return currentClient;
    }

    public void setCurrentClient(Client currentClient) {
        this.currentClient = currentClient;
    }

    public Converter getStatusConverter() {
        return statusConverter;
    }

    public List<Client> getFilteredClients() {
        return filteredClients;
    }

    public void setFilteredClients(List<Client> filteredClients) {
        this.filteredClients = filteredClients;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

}
