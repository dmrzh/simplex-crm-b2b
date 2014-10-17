package ru.dev_server.client.model;

import org.hibernate.annotations.Type;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.util.HashSet;
import java.util.Set;

/**. */

@Entity
public class DynamicColumn extends LongIdPersistentEntity{

    @ManyToOne
    private Company company;
    @Enumerated(EnumType.STRING)
    private ExstendedTables exstendedTables;
    private Class type;
    private String name;
    private String description;
    @Type(type="yes_no")
    private boolean showInList;


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="Labels")
    @Column(name="label")
    private java.util.Set<String> labels = new HashSet<String>();

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public ExstendedTables getExstendedTables() {
        return exstendedTables;
    }

    public void setExstendedTables(ExstendedTables exstendedTables) {
        this.exstendedTables = exstendedTables;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isShowInList() {
        return showInList;
    }

    public void setShowInList(boolean showInList) {
        this.showInList = showInList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getLabels() {
        return labels;
    }

    public void setLabels(Set<String> labels) {
        this.labels = labels;
    }
}

