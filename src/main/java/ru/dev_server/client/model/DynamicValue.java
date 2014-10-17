package ru.dev_server.client.model;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**. */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class DynamicValue extends LongIdPersistentEntity{

    @ManyToOne
    private Client clientValue;

    @ManyToOne
    private DynamicColumn dynamicColumn;


    private String value;

    private Date dateValue;

    private Boolean booleanValue;

    private Double doubleValue;

    public Boolean getBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DynamicColumn getDynamicColumn() {
        return dynamicColumn;
    }

    public void setDynamicColumn(DynamicColumn dynamicColumn) {
        this.dynamicColumn = dynamicColumn;
    }

    public Client getClientValue() {
        return clientValue;
    }

    public void setClientValue(Client clientValue) {
        this.clientValue = clientValue;
    }

    public Date getDateValue() {
        return dateValue;
    }

    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    public Double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(Double doubleValue) {
        this.doubleValue = doubleValue;
    }

    private static DateFormat DATE_FORMAT=SimpleDateFormat.getDateInstance(DateFormat.MEDIUM,new Locale("ru","RU"));
    @Override
    public String toString() {
        if(dynamicColumn.getType()==Date.class){
            if(dateValue==null){
                return "";
            }else{
                return DATE_FORMAT.format(dateValue);
            }
        }else if(dynamicColumn.getType()==Client.class){

            Client clientValue1 = getClientValue();
            if(clientValue1==null){
                return "";
            }else {
                return clientValue1.getFio();
            }
        }else if(dynamicColumn.getType()==Boolean.class){
            if(booleanValue==null){
                return "";
            }else{
                return booleanValue?"Да":"Нет";
            }
        }else if(dynamicColumn.getType()==Double.class){
            if(doubleValue==null){
                return "";
            }else{
                Double.toString(doubleValue);
            }
        }
        if(value==null){
            return "";
        }else{
            return value;
        }


    }

}
