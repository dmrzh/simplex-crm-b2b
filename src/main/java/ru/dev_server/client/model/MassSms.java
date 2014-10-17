package ru.dev_server.client.model;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**.*/
@Entity
public class MassSms extends  LongIdPersistentEntity{
    @ManyToOne      @NotNull
    private Company company;
    private String name;
    @Length(max=1024, min=1)
    private String text;
    private Date startDate;
    @Type(type="yes_no")
    private boolean active =false;


    @OneToMany(mappedBy = "massSms",orphanRemoval=true, cascade = CascadeType.ALL )
    @MapKey(name = "client")
    private Map<Client,SmsNotification> smsNotifications=new HashMap<Client,SmsNotification>();


    @Type(type="yes_no")  @Index(name = "deletedIdx")
    private boolean deleted=false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Map<Client, SmsNotification> getSmsNotifications() {
        return smsNotifications;
    }

    public void setSmsNotifications(Map<Client, SmsNotification> smsNotifications) {
        this.smsNotifications = smsNotifications;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
