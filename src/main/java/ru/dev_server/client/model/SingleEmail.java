package ru.dev_server.client.model;

import org.hibernate.annotations.Index;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**.*/
@Entity
public class SingleEmail extends LongIdPersistentEntity{
    @Size(max=255)
    private String subject;
    @Size(max=2048)
    private String text;

    @ManyToOne
    private Employee from;

    @ManyToOne
    private Client to;

    private Date startDate;
    @NotNull
    @Enumerated(EnumType.STRING) @Index(name = "emailNotificationStatusIdx")
    private NotificationStatus notificationStatus=NotificationStatus.WAITING;

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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public NotificationStatus getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(NotificationStatus notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public Employee getFrom() {
        return from;
    }

    public void setFrom(Employee from) {
        this.from = from;
    }

    public Client getTo() {
        return to;
    }

    public void setTo(Client to) {
        this.to = to;
    }
}
