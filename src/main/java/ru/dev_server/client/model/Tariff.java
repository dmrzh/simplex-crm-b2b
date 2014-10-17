package ru.dev_server.client.model;

import org.hibernate.annotations.Type;

import javax.persistence.Entity;

/**.*/
@Entity
public class Tariff extends LongIdPersistentEntity{
    private Class aClass;
    /**
     * price of one sms, kopeek.
     */
    private int constantCost;
    @Type(type="yes_no")
    private boolean defaultTariff=false;

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }

    public int getConstantCost() {
        return constantCost;
    }

    public void setConstantCost(int constantCost) {
        this.constantCost = constantCost;
    }

    public boolean isDefaultTariff() {
        return defaultTariff;
    }

    public void setDefaultTariff(boolean defaultTariff) {
        this.defaultTariff = defaultTariff;
    }
}
