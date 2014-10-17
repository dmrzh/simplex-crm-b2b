package ru.dev_server.client.model;

import org.hibernate.annotations.Index;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Client category.
 */
@Entity
public class Category extends LongIdPersistentEntity {

    @NotNull @Size(max = 255,min = 1)
    private String name;

    @ManyToOne @NotNull  @Index (name = "category_company_idx")
    private Company company;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
