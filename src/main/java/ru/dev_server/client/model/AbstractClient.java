package ru.dev_server.client.model;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * .
 */

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class AbstractClient extends LongIdPersistentEntity {
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "client",orphanRemoval = true)
    @Cascade(CascadeType.ALL)
    @IndexColumn(name="index", base=10)
//    @OrderColumn(name="index")
    private List<Contact> contacts=new ArrayList<Contact>();

    @Type(type="yes_no")  @Index(name = "client_deleted_idx")
    private boolean deleted=false;

    private Date creationDate=new Date();


    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
