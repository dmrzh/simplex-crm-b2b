package ru.dev_server.client.model;

import org.hibernate.annotations.Index;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Client with notification status.
 */
@Entity
public class SmsNotification extends LongIdPersistentEntity{

    @ManyToOne @NotNull
    private Client client;

    @ManyToOne
    private Meeting meeting;

    @ManyToOne
    private MassSms massSms;

    @OneToOne(mappedBy = "smsNotification")
    private SingleSms singleSms;

    @NotNull    @Enumerated(EnumType.STRING) @Index(name = "notificationStatusIdx")
    private NotificationStatus notificationStatus=NotificationStatus.WAITING;

    private String sendError;

    private String sendFrom;

    private String sendTo;


    private String note;


    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL)
    private List<SmppStatus> smppStatusList;


    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public NotificationStatus getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(NotificationStatus notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public String getSendError() {
        return sendError;
    }

    public void setSendError(String sendError) {
        this.sendError = sendError;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public MassSms getMassSms() {
        return massSms;
    }


    public void setMassSms(MassSms massSms) {
        this.massSms = massSms;
    }

    public String getSendFrom() {
        return sendFrom;
    }

    public void setSendFrom(String sendFrom) {
        this.sendFrom = sendFrom;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public SingleSms getSingleSms() {
        return singleSms;
    }

    public void setSingleSms(SingleSms singleSms) {
        this.singleSms = singleSms;
    }

    public List<SmppStatus> getSmppStatusList() {
        return smppStatusList;
    }

    public void setSmppStatusList(List<SmppStatus> smppStatusList) {
        this.smppStatusList = smppStatusList;
    }


    public Date getStartDate(){
        if(meeting!=null){
            return meeting.getNotificationDate();
        }
        if(massSms!=null){
            return massSms.getStartDate();
        }
        if(singleSms !=null){
            return singleSms.getStartDate();
        }
        throw new IllegalStateException("meeting AND massSms AND cant be null" );
    }
    public String getTextTemlate(){
        if(meeting!=null){
            return meeting.getSmsText();
        }
        if(massSms!=null){
            return massSms.getText();
        }

        if(singleSms !=null){
            return singleSms.getText();
        }
        throw new IllegalStateException("meeting AND massSms AND singleSms cant be null" );


    }
}
