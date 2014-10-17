package ru.dev_server.client.model;

import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 *  .
 */

@Entity
public  class ItEvent extends LongIdPersistentEntity {

    @NotNull
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    private Date eventTime=new Date();
    private String text;
    @ManyToOne
    @JoinTable(name="ItEvent_Employee",
            joinColumns = @JoinColumn(name="IT_EVENT_ID"),
            inverseJoinColumns = @JoinColumn(name="EMPLOYEE_ID")
    )
    private Employee owner;

    @ManyToOne
    @JoinTable(name="ItEvent_client",
            joinColumns = @JoinColumn(name="IT_EVENT_ID"),
            inverseJoinColumns = @JoinColumn(name="ABSTRACT_CLIENT_ID")
    )
    private AbstractClient client;

    @Type(type="yes_no")
    private boolean remind;

    @Type(type="yes_no")
    private boolean completed;
    private Double rub;
    private Double euro;

    static public enum EventType{
        BILL, CALL, MEETING,
    }


    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Employee getOwner() {
        return owner;
    }

    public void setOwner(Employee owner) {
        this.owner = owner;
    }



    public boolean isRemind() {
        return remind;
    }

    public void setRemind(boolean remind) {
        this.remind = remind;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Double getRub() {
        return rub;
    }

    public void setRub(Double rub) {
        this.rub = rub;
    }

    public Double getEuro() {
        return euro;
    }

    public void setEuro(Double euro) {
        this.euro = euro;
    }

    public AbstractClient getClient() {
        return client;
    }

    public void setClient(AbstractClient client) {
        this.client = client;
    }

    public String getSumm(){
       String res="";
        if(getRub()!=null){
            res=res+getRub() + " Ք ";
        }
        if(getEuro()!=null){
            res=res+getEuro() + " €";
        }
        return res;
    }
}
