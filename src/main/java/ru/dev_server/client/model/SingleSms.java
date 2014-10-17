package ru.dev_server.client.model;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.Date;

/**.*/
@Entity
public class SingleSms extends LongIdPersistentEntity{
    private String text;
    private Date startDate;

    @OneToOne()
    private SmsNotification smsNotification;

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

    public SmsNotification getSmsNotification() {
        return smsNotification;
    }

    public void setSmsNotification(SmsNotification smsNotification) {
        this.smsNotification = smsNotification;
    }
}
