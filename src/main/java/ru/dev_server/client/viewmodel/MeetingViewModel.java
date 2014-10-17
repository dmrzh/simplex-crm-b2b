package ru.dev_server.client.viewmodel;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.Converter;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import ru.dev_server.client.AuthService;
import ru.dev_server.client.DynamicaColumnUtils;
import ru.dev_server.client.I18nConverter;
import ru.dev_server.client.dao.ClientDAO;
import ru.dev_server.client.dao.DynamicColumnDAO;
import ru.dev_server.client.dao.MeetingDAO;
import ru.dev_server.client.dao.SmsNotificationDAO;
import ru.dev_server.client.model.Client;
import ru.dev_server.client.model.Company;
import ru.dev_server.client.model.DynamicColumn;
import ru.dev_server.client.model.Employee;
import ru.dev_server.client.model.ExstendedTables;
import ru.dev_server.client.model.Meeting;
import ru.dev_server.client.model.NotificationStatus;
import ru.dev_server.client.model.SmsNotification;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**.*/
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class MeetingViewModel {
    private static final Logger LOG= LoggerFactory.getLogger(MeetingViewModel.class);
    private Meeting currentMeeting;

    @WireVariable("clientDAO")
    private ClientDAO clientDAO;
    @WireVariable("meetingDAO")
    private MeetingDAO meetingDAO;
    @WireVariable("authService")
    private AuthService authService;
    private SmsNotificationDAO smsNotificationDAO;
    @WireVariable
    DynamicColumnDAO dynamicColumnDAO;
    @WireVariable
    private DynamicaColumnUtils dynamicaColumnUtils;

    Window meetingWin;
    private List<DynamicColumn> dynamicColumnList=new ArrayList<DynamicColumn>();

    private List<Employee> employeeList;

    private Converter statusConverter =new I18nConverter(NotificationStatus.class);

    private Date beginTimeOnly;
    private Date endTimeOnly;


    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view,@ExecutionArgParam("meeting") Meeting meeting){
        meetingWin=view;
        setCurrentMeeting(meeting);
    }
    @Init
    public void init(){
        Company company = authService.getCompany();
        dynamicColumnList=dynamicColumnDAO.findAll(company, ExstendedTables.MEETING);
        employeeList=company.getEmployeeList();
    }


    @Command
    public void cancel(){
        meetingWin.detach();
    }


    @Command
    public void delete(){
        currentMeeting=meetingDAO.merge(currentMeeting);
        meetingDAO.delete(currentMeeting);
        meetingWin.detach();
    }

    @Command
    public void openGroup(){
        HashMap<String, Object> arg = new HashMap<String, Object>();
        arg.put("meeting", getCurrentMeeting());
        Executions.createComponents("/WEB-INF/zul/groupMeeting.zul",meetingWin, arg);
    }

    @Command
    public void newClient(){
        Client client = dynamicaColumnUtils.createClient();
        currentMeeting.setClient(client);
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("client",client);
        BindUtils.postGlobalCommand(null, null, "editClient", args);
    }
    @GlobalCommand  @NotifyChange("*")
    public  void refreshClients() {
    }


    @Command
    public void save(){
        saveTimeOnly();
        DateTime begin = new DateTime(currentMeeting.getBeginDate());
        DateTime end = new DateTime(currentMeeting.getEndDate());
        Duration duration = new Duration(begin, end);
        if(duration.toStandardMinutes().getMinutes()<=0){
            Messagebox.show("Дата начала встречи должна быть раньше даты конца","Ошибка",
                    Messagebox.OK,
                    Messagebox.ERROR);
            return;
        }else{
            Client c = currentMeeting.getClient();
            if(c !=null && c.getId()==null){//new client not saved, canceled
                currentMeeting.setClient(null);
            }
            meetingDAO.saveOrUpdate(currentMeeting);
            meetingWin.detach();
        }
    }

    private void saveTimeOnly() {
        Date beginDate = currentMeeting.getBeginDate();
        beginDate.setHours(beginTimeOnly.getHours());
        beginDate.setMinutes(beginTimeOnly.getMinutes());


        Date endDate = currentMeeting.getEndDate();
        endDate.setHours(endTimeOnly.getHours());
        endDate.setMinutes(endTimeOnly.getMinutes());
    }


    public Meeting getCurrentMeeting() {
        return currentMeeting;
    }

    public void setCurrentMeeting(Meeting currentMeeting) {
        this.currentMeeting = currentMeeting;
        saveTimeOnly(currentMeeting);
    }

    private void saveTimeOnly(Meeting currentMeeting) {
        beginTimeOnly=(Date)currentMeeting.getBeginDate().clone();
        endTimeOnly=(Date)currentMeeting.getEndDate().clone();
    }


    public Converter getStatusConverter() {
        return statusConverter;
    }

    public void setStatusConverter(Converter statusConverter) {
        this.statusConverter = statusConverter;
    }

    public List<DynamicColumn> getDynamicColumnList() {
        return dynamicColumnList;
    }

    public void setDynamicColumnList(List<DynamicColumn> dynamicColumnList) {
        this.dynamicColumnList = dynamicColumnList;
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    public Date getBeginTimeOnly() {
        return beginTimeOnly;
    }

    public void setBeginTimeOnly(Date beginTimeOnly) {
        this.beginTimeOnly = beginTimeOnly;
    }

    public Date getEndTimeOnly() {
        return endTimeOnly;
    }

    public void setEndTimeOnly(Date endTimeOnly) {
        this.endTimeOnly = endTimeOnly;
    }

    public Client getClient() {
        return currentMeeting.getClient();
    }

    public void setClient(Client client) {
        if(currentMeeting.getId() !=null){
            currentMeeting=meetingDAO.get(currentMeeting.getId());
        }
        Client oldClient = currentMeeting.getClient();
        SmsNotification oldNtification = currentMeeting.getNotifications().get(oldClient);

        boolean persistenClient = currentMeeting.getClient() != null && currentMeeting.getClient().getId() != null;
        boolean winthoutClient = (!currentMeeting.getClientList().contains(oldClient));
        if(persistenClient && winthoutClient){
           // smsNotificationDAO.delete(oldNtification);
            currentMeeting.getNotifications().remove(oldClient);
        }

        if(currentMeeting.getNotifications().get(client)==null){
            SmsNotification value = new SmsNotification();
            value.setMeeting(currentMeeting);
            value.setClient(client);
            currentMeeting.getNotifications().put(client, value);
        }
        currentMeeting.setClient(client);
    }
}
