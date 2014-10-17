package ru.dev_server.client.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**. */
@Entity
public class Payment extends LongIdPersistentEntity {
    public enum PaymentState{
        PAYING, PAYED, ERROR
    }

    @ManyToOne  @NotNull
    private Company company;

    @NotNull
    private Date startDate = new Date();

    private Date payedDate=null;

    private String note=null;


    @NotNull    @Enumerated(EnumType.STRING)
    private PaymentState paymentState=PaymentState.PAYING;

    private long amount=0;

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public PaymentState getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(PaymentState paymentState) {
        this.paymentState = paymentState;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getPayedDate() {
        return payedDate;
    }

    public void setPayedDate(Date payedDate) {
        this.payedDate = payedDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
