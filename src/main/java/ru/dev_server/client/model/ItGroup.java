package ru.dev_server.client.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * .
 */
@Entity
public class ItGroup extends LongIdPersistentEntity{
    private String name;

    @ManyToOne
    private Employee owner;

    @ManyToOne
    private Company company;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Employee getOwner() {
        return owner;
    }

    public void setOwner(Employee owner) {
        this.owner = owner;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
