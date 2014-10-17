package ru.dev_server.client.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**.*/
@Entity
public class Contact extends LongIdPersistentEntity {

    public Contact() {
    }

    public Contact(String value) {
        this.value = value;
    }
    @ManyToOne (optional = false) @NotNull
    private AbstractClient client;

    @Enumerated(EnumType.STRING)
    private ContactType contactType=ContactType.MOBILE;
    private String value;
    private String note;

    public ContactType getContactType() {
        return contactType;
    }

    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public AbstractClient getClient() {
        return client;
    }

    public void setClient(AbstractClient client) {
        this.client = client;
    }
}

