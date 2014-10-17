package ru.dev_server.client.model;

import org.hibernate.validator.constraints.Email;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.security.SecureRandom;

/**.*/
@Entity
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"id"}),
        @UniqueConstraint(columnNames = {"email"})
    }
)
public class Employee extends LongIdPersistentEntity{

    @NotNull @Email @Basic
    private String email;
    @NotNull  @Size(min=4, max=32)
    private String password;
    private String fio;
    @ManyToOne   @NotNull
    private Company company;
    private String role="ROLE_EMPLOYEE";
    private boolean showOnlyMyMeetings=false;
    private boolean disable=false;

    //remove email activation
    private String activationCode;//=""+Math.abs(secureRandom.nextLong());


    static SecureRandom secureRandom = new SecureRandom();

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isShowOnlyMyMeetings() {
        return showOnlyMyMeetings;
    }

    public void setShowOnlyMyMeetings(boolean showOnlyMyMeetings) {
        this.showOnlyMyMeetings = showOnlyMyMeetings;
    }

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }
}
