package ru.dev_server.client.model;

import org.jsmpp.util.DeliveryReceiptState;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**.*/
@Entity
public class SmppStatus extends LongIdPersistentEntity{

    public SmppStatus() {
    }

    @ManyToOne @NotNull
    private SmsNotification notification;

    @Enumerated(EnumType.STRING)
    private DeliveryReceiptState smppFinalStatus;

    private Long smppId;

    private Integer commandStatus;


    private String sendError;

    public DeliveryReceiptState getSmppFinalStatus() {
        return smppFinalStatus;
    }

    public void setSmppFinalStatus(DeliveryReceiptState smppFinalStatus) {
        this.smppFinalStatus = smppFinalStatus;
    }

    public Long getSmppId() {
        return smppId;
    }

    public void setSmppId(Long smppId) {
        this.smppId = smppId;
    }

    public Integer getCommandStatus() {
        return commandStatus;
    }

    public void setCommandStatus(Integer commandStatus) {
        this.commandStatus = commandStatus;
    }

    public SmsNotification getNotification() {
        return notification;
    }

    public void setNotification(SmsNotification notification) {
        this.notification = notification;
    }

    public String getSendError() {
        return sendError;
    }

    public void setSendError(String sendError) {
        this.sendError = sendError;
    }
}
