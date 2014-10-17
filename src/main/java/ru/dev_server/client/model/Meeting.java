package ru.dev_server.client.model;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**.*/
@Entity
public class Meeting extends LongIdPersistentEntity{
    @ManyToOne
    private Client client;


    @ManyToMany()
    private Set<Client> clientList = new HashSet<Client>();


    @OneToMany(mappedBy="meeting",orphanRemoval=true, cascade = CascadeType.ALL)
    @MapKey(name = "client")

    private Map<Client, SmsNotification> notifications=new HashMap<Client, SmsNotification>();

    @ManyToOne
    private Employee employee;

    @Column(name = "beginDate", nullable = false) @NotNull
    private Date beginDate;
    @Column(name = "endDate", nullable = false)   @NotNull
    private Date endDate;
    private String note;
    @NotNull
    private String smsText="";


    private Date notificationDate;

    private Integer rememberBeforeHours;




    @OneToMany(mappedBy = "meeting",orphanRemoval=true, cascade = CascadeType.ALL )
    @MapKey(name = "dynamicColumn")
    private Map<DynamicColumn,MeetingDynamicValue> dynamicValueList=new HashMap<DynamicColumn, MeetingDynamicValue>();


    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date startDate) {
        this.beginDate = startDate;
        //update notificationDate
        setRememberBeforeHours(getRememberBeforeHours());
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getRememberBeforeHours() {
        return rememberBeforeHours;
    }

    public void setRememberBeforeHours(Integer rememberBeforeHours) {
        this.rememberBeforeHours = rememberBeforeHours;
        if(rememberBeforeHours!=null&new Date().before(beginDate)){
            notificationDate=new Date(this.beginDate.getTime()-1000*60*60*rememberBeforeHours);
        }else {
            notificationDate=null;
        }
    }

    public String getSmsText() {
        return smsText;
    }

    public void setSmsText(String smsText) {
        this.smsText = smsText;
    }

    public Date getNotificationDate() {
        return notificationDate;
    }


    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Map<DynamicColumn, MeetingDynamicValue> getDynamicValueList() {
        return dynamicValueList;
    }

    public void setDynamicValueList(Map<DynamicColumn, MeetingDynamicValue> dynamicValueList) {
        this.dynamicValueList = dynamicValueList;
    }

    public void addClient(Client client){
        if(client!=null){
            if(! notifications.containsKey(client)){
                SmsNotification smsNotification = new SmsNotification();
                smsNotification.setClient(client);
                smsNotification.setMeeting(this);
                notifications.put(client, smsNotification);
            }
            getClientList().add(client);
        }

    }

    public Set<Client> getClientList() {
        return clientList;
    }

    public void setClientList(Set<Client> clientList) {
        this.clientList = clientList;
    }

    public Map<Client, SmsNotification> getNotifications() {
        return notifications;
    }

    public void setNotifications(Map<Client, SmsNotification> notifications) {
        this.notifications = notifications;
    }
    public SmsNotification getNotification(){
        if(client==null){
            return null;
        }
        return notifications.get(client);
    }
}
