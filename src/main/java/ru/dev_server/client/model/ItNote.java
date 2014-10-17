package ru.dev_server.client.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * .
 */
@Entity
public class ItNote extends LongIdPersistentEntity{
    @ManyToOne
    private JuridicalPerson juridicalPerson;
    @ManyToOne
    private Employee author;

    @NotNull
    private Date date = new Date();

    @Column(length = 1024)
    private String text;

    public JuridicalPerson getJuridicalPerson() {
        return juridicalPerson;
    }

    public void setJuridicalPerson(JuridicalPerson juridicalPerson) {
        this.juridicalPerson = juridicalPerson;
    }

    public Employee getAuthor() {
        return author;
    }

    public void setAuthor(Employee author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
