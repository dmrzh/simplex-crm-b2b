package ru.dev_server.client.model;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.security.SecureRandom;
import java.util.Date;

/**.*/
@Entity
public class PasswordReset extends LongIdPersistentEntity {
    private final static SecureRandom secureRandom = new SecureRandom();
    @OneToOne @NotNull
    private Employee employee;
    private String resetConfimation=""+Math.abs(secureRandom.nextLong());
    private Date date=new Date();


    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getResetConfimation() {
        return resetConfimation;
    }

    public void setResetConfimation(String resetConfimation) {
        this.resetConfimation = resetConfimation;
    }

    public Date getDate() {
        return date;
    }
}
