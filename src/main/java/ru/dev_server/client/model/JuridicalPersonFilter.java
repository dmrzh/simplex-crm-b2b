package ru.dev_server.client.model;

import java.util.ArrayList;
import java.util.List;

/**
 * .
 */
public class JuridicalPersonFilter {
    private String name="";
    private String city="";
    private String region="";
    private String address="";

    private String fax="";
    private String contact="";
    private String site="";
    private List<ItGroup> selectedPublicGroups=new ArrayList<ItGroup>();
    private List<ItGroup> selectedPrivateGroups=new ArrayList<ItGroup>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }




    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public List<ItGroup> getSelectedPublicGroups() {
        return selectedPublicGroups;
    }

    public void setSelectedPublicGroups(List<ItGroup> selectedPublicGroups) {
        this.selectedPublicGroups = selectedPublicGroups;
    }

    public List<ItGroup> getSelectedPrivateGroups() {
        return selectedPrivateGroups;
    }

    public void setSelectedPrivateGroups(List<ItGroup> selectedPrivateGroups) {
        this.selectedPrivateGroups = selectedPrivateGroups;
    }


    public static String wrapFullSearch(String var){
        if(var==null || "".equals(var) ){
            return "";
        }else{
            return "%"+var+"%";
        }

    }
}
