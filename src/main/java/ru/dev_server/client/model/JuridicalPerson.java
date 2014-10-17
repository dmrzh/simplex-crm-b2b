package ru.dev_server.client.model;


import org.hibernate.annotations.Index;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * .
 */
@Entity
public class JuridicalPerson extends AbstractClient{


    private String name;
    private String city;
    private String address;
    private String region;
    private String site;

    private String phones;
    private String fax;
    @OneToMany(mappedBy = "juridicalPerson")
    private List<Client> contactPerson= new ArrayList<Client>();

    @ManyToMany()
    @JoinTable(name = "jurid_person_group",
            joinColumns = { @JoinColumn(name = "juridicalperson_id") },
            inverseJoinColumns = { @JoinColumn(name = "group_id") })
    private List<ItGroup> publicGroups= new ArrayList<ItGroup>();

    @ManyToMany()
    @JoinTable(name = "jurid_person_group_priv",
            joinColumns = { @JoinColumn(name = "juridicalperson_id") },
            inverseJoinColumns = { @JoinColumn(name = "group_id") })
    private List<ItGroup> privateGroups= new ArrayList<ItGroup>();

    private String note;


    @ManyToOne
    @NotNull
    @Index(name = "juridicalPerson_company_idx")
    private Company company;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public List<Client> getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(List<Client> contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public List<ItGroup> getPublicGroups() {
        return publicGroups;
    }

    public void setPublicGroups(List<ItGroup> publicGroups) {
        this.publicGroups = publicGroups;
    }

    public List<ItGroup> getPrivateGroups() {
        return privateGroups;
    }

    public void setPrivateGroups(List<ItGroup> privateGroups) {
        this.privateGroups = privateGroups;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhones() {
        return phones;
    }

    public void setPhones(String phones) {
        this.phones = phones;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }
}
