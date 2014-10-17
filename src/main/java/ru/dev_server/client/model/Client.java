package ru.dev_server.client.model;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Index;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * .
 */
@Entity
public class Client extends AbstractClient{




    @Index(name = "nameIdx")
    private String name;

    @ManyToOne
    private JuridicalPerson juridicalPerson;


    @ManyToOne
    private Category category;
    private String address;
    private String note;
    private String phones;
    private String email;

    private String source;
    @ManyToMany(mappedBy = "client" )
    private List<Meeting> meetings;

    @OneToMany (mappedBy = "client",fetch=FetchType.EAGER)
    @Cascade(CascadeType.ALL)

    @MapKey(name = "dynamicColumn")
    private Map<DynamicColumn,ClientDynamicValue> dynamicValueList=new HashMap<DynamicColumn, ClientDynamicValue>();

    @ManyToOne  @NotNull @Index(name = "client_company_idx")
    private Company company;




    public Client() {
    }

    public String getFio(){
        return name;
    }



    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<Meeting> getMeetings() {
        return meetings;
    }

    public void setMeetings(List<Meeting> meetings) {
        this.meetings = meetings;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public JuridicalPerson getJuridicalPerson() {
        return juridicalPerson;
    }

    public void setJuridicalPerson(JuridicalPerson juridicalPerson) {
        this.juridicalPerson = juridicalPerson;
    }

    public Map<DynamicColumn, ClientDynamicValue> getDynamicValueList() {
        return dynamicValueList;
    }

    public void setDynamicValueList(Map<DynamicColumn, ClientDynamicValue> dynamicValueList) {
        this.dynamicValueList = dynamicValueList;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
