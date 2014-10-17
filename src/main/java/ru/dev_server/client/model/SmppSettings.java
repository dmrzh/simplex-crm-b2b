package ru.dev_server.client.model;

import org.jsmpp.bean.MessageClass;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.TypeOfNumber;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

/**.*/
@Entity
public class SmppSettings extends LongIdPersistentEntity{
    @NotNull
    private String host;
    @NotNull
    private int port;
    @NotNull
    private String login;
    @NotNull
    private String password;

    @Enumerated(EnumType.STRING)
    private TypeOfNumber sourceAddrTon;
    @Enumerated(EnumType.STRING)
    private NumberingPlanIndicator sourceAddrNpi;
    @Enumerated(EnumType.STRING)
    private TypeOfNumber destAddrTon;
    @Enumerated(EnumType.STRING)
    private NumberingPlanIndicator destAddrNpi;
    private int priorityFlag;
    //#CTM

    private String  serviceType;

    @Enumerated(EnumType.STRING)
    private MessageClass messageClass;

    public NumberingPlanIndicator getDestAddrNpi() {
        return destAddrNpi;
    }

    public void setDestAddrNpi(NumberingPlanIndicator destAddrNpi) {
        this.destAddrNpi = destAddrNpi;
    }

    public TypeOfNumber getDestAddrTon() {
        return destAddrTon;
    }

    public void setDestAddrTon(TypeOfNumber destAddrTon) {
        this.destAddrTon = destAddrTon;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public MessageClass getMessageClass() {
        return messageClass;
    }

    public void setMessageClass(MessageClass messageClass) {
        this.messageClass = messageClass;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPriorityFlag() {
        return priorityFlag;
    }

    public void setPriorityFlag(int priorityFlag) {
        this.priorityFlag = priorityFlag;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public NumberingPlanIndicator getSourceAddrNpi() {
        return sourceAddrNpi;
    }

    public void setSourceAddrNpi(NumberingPlanIndicator sourceAddrNpi) {
        this.sourceAddrNpi = sourceAddrNpi;
    }

    public TypeOfNumber getSourceAddrTon() {
        return sourceAddrTon;
    }

    public void setSourceAddrTon(TypeOfNumber sourceAddrTon) {
        this.sourceAddrTon = sourceAddrTon;
    }
}
