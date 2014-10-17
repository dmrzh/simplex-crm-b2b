package ru.dev_server.client.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**.*/
@Entity
public class Company extends LongIdPersistentEntity{
    @NotEmpty
    private String name;
    @OneToMany(mappedBy = "company")
    private List<Employee> employeeList;

    @OneToMany(mappedBy = "company")
    private List<Client> clientList;

    private String smsFrom="";

    private String timezone="Europe/Moscow=GMT+4";

    /**
     * balance in kopeek.
     */
    private long balance=100l;

    @ManyToOne @NotNull
    private Tariff currentTariff;

    @OneToOne
    private Company referer;
    @Max(24) @Min(0)
    private Integer startWorkingHours=8;
    @Max(24) @Min(0)
    private Integer finishWorkingHours=20;
    @Size(max=15, min=1)  @NotNull
    private String companyPhone="simplex-crm";

    @OneToMany (mappedBy = "company")
    private List<DynamicColumn> dynamicColumnList;

    @Basic @NotNull
    private Date registredDate= new Date();

    @Size(max=255)
    private String  defaultSmsText="";

    @Min(0) @Max(24*30)
    private Integer defaultReminderTime=1;

    @OneToOne(mappedBy = "company")
    private SmtpSettings smtpSettings;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    public List<Client> getClientList() {
        return clientList;
    }

    public void setClientList(List<Client> clientList) {
        this.clientList = clientList;
    }

    public Company getReferer() {
        return referer;
    }

    public void setReferer(Company referer) {
        this.referer = referer;
    }

    public String getSmsFrom() {
        return smsFrom;
    }

    public void setSmsFrom(String smsFrom) {
        this.smsFrom = smsFrom;
    }

    public Integer getStartWorkingHours() {
        return startWorkingHours;
    }

    public void setStartWorkingHours(Integer startWorkingHours) {
        this.startWorkingHours = startWorkingHours;
    }

    public Integer getFinishWorkingHours() {
        return finishWorkingHours;
    }

    public void setFinishWorkingHours(Integer finishWorkingHours) {
        this.finishWorkingHours = finishWorkingHours;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getDefaultSmsText() {
        return defaultSmsText;
    }

    public void setDefaultSmsText(String defaultSmsText) {
        this.defaultSmsText = defaultSmsText;
    }

    public Integer getDefaultReminderTime() {
        return defaultReminderTime;
    }

    public void setDefaultReminderTime(Integer defaultReminderTime) {
        this.defaultReminderTime = defaultReminderTime;
    }

    public Date getRegistredDate() {
        return registredDate;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public SmtpSettings getSmtpSettings() {
        return smtpSettings;
    }

    public void setSmtpSettings(SmtpSettings smtpSettings) {
        this.smtpSettings = smtpSettings;
    }

    public Tariff getCurrentTariff() {
        return currentTariff;
    }

    public void setCurrentTariff(Tariff currentTariff) {
        this.currentTariff = currentTariff;
    }
}
