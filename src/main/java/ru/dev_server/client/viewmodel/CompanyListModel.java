package ru.dev_server.client.viewmodel;

import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import ru.dev_server.client.dao.ClientDAO;
import ru.dev_server.client.dao.CompanyDAO;
import ru.dev_server.client.dao.MeetingDAO;
import ru.dev_server.client.model.Company;

import java.util.List;

/**. */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class CompanyListModel {
    @WireVariable("companyDAO")
    CompanyDAO companyDAO;

    @WireVariable
    ClientDAO clientDAO;

    @WireVariable
    MeetingDAO meetingDAO;

    private List<Company> companies;

    @Init
    public void init(){
        companies=companyDAO.findAll();
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }
    public int getClients(Company company){
        return clientDAO.findAll(company).size();
    }

    public int getMeetings(Company company){
        return meetingDAO.findAll(company).size();
    }
}
