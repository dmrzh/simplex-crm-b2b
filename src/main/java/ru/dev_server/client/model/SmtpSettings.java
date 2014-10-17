package ru.dev_server.client.model;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**.
 */
@Entity
public class SmtpSettings extends LongIdPersistentEntity {
    @OneToOne
    private Company company;
    private String mailSmtpHost="";
    private int mailSmtpPort=465;
    private String mailTransportProtocol="smtp";
    private boolean mailSmtpAuth=true;


    private String smtpUsername="";
    private String smtpPassword="";


    private String mailSmtpSocketFactoryClass="javax.net.ssl.SSLSocketFactory";
    private Boolean mailSmtpSocketFactoryFallback=false;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public boolean isMailSmtpAuth() {
        return mailSmtpAuth;
    }

    public void setMailSmtpAuth(boolean mailSmtpAuth) {
        this.mailSmtpAuth = mailSmtpAuth;
    }

    public String getMailSmtpHost() {
        return mailSmtpHost;
    }

    public void setMailSmtpHost(String mailSmtpHost) {
        this.mailSmtpHost = mailSmtpHost;
    }

    public int getMailSmtpPort() {
        return mailSmtpPort;
    }

    public void setMailSmtpPort(int mailSmtpPort) {
        this.mailSmtpPort = mailSmtpPort;
    }

    public String getMailSmtpSocketFactoryClass() {
        return mailSmtpSocketFactoryClass;
    }

    public void setMailSmtpSocketFactoryClass(String mailSmtpSocketFactoryClass) {
        this.mailSmtpSocketFactoryClass = mailSmtpSocketFactoryClass;
    }

    public Boolean getMailSmtpSocketFactoryFallback() {
        return mailSmtpSocketFactoryFallback;
    }

    public void setMailSmtpSocketFactoryFallback(Boolean mailSmtpSocketFactoryFallback) {
        this.mailSmtpSocketFactoryFallback = mailSmtpSocketFactoryFallback;
    }

    public String getMailTransportProtocol() {
        return mailTransportProtocol;
    }

    public void setMailTransportProtocol(String mailTransportProtocol) {
        this.mailTransportProtocol = mailTransportProtocol;
    }

    public String getSmtpPassword() {
        return smtpPassword;
    }

    public void setSmtpPassword(String smtpPassword) {
        this.smtpPassword = smtpPassword;
    }

    public String getSmtpUsername() {
        return smtpUsername;
    }

    public void setSmtpUsername(String smtpUsername) {
        this.smtpUsername = smtpUsername;
    }
}
