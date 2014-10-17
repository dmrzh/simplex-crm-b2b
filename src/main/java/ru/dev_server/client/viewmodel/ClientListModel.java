package ru.dev_server.client.viewmodel;

import org.zkoss.zul.AbstractListModel;
import org.zkoss.zul.event.ListDataEvent;
import ru.dev_server.client.AuthService;
import ru.dev_server.client.dao.ClientDAO;
import ru.dev_server.client.model.Client;
import ru.dev_server.client.model.Company;

import java.util.Comparator;
import java.util.List;

/**.*/
public class ClientListModel extends AbstractListModel<Client> implements org.zkoss.zul.ext.Sortable{
    private Long[] clientIds;
    private ClientDAO clientDAO;
    private AuthService authService;

    private String filter;
    private int PAGE_SIZE = 50;

    public ClientListModel(ClientDAO clientDAO, AuthService authService) {
        this.clientDAO = clientDAO;
        this.authService = authService;
        clientIds =new Long[0];
        clearData();
        setMultiple(true);
    }

    @Override
    public Client getElementAt(int index) {

        if(clientIds[index]==null){
            int i=0;
            for(Client c:getPageByIndex(index)){
                clientIds[index+(i++)]=c.getId();
            }
        }
        Client client = clientDAO.get(clientIds[index]);
        return client;
    }

    private List<Client> getPageByIndex(int index) {
        List<Client> all;
        Company company = authService.getCompany();
        if(filter==null || filter.length()==0){
            all = clientDAO.findAll(company, index, PAGE_SIZE);
        }else{
           all=clientDAO.findByFilter(filter,company,index,PAGE_SIZE);
        }
        return all;
    }

    public void clearData(){
        clearSelection();
        long clientCount;
        if(filter==null || filter.length()==0){
            clientCount= clientDAO.getClientCount(authService.getCompany());
        }else{
            clientCount= clientDAO.getClientCountByFilter(filter,authService.getCompany());
        }
        int oldClientCount= clientIds.length;
        clientIds =new Long[((int)clientCount)];
        if(oldClientCount>0){
            fireEvent(ListDataEvent.CONTENTS_CHANGED, -1, -1);
        }
    }

    @Override
    public int getSize() {
        return clientIds.length;
    }

    @Override
    public String getSortDirection(Comparator comparator) {
        return null;
    }

    @Override
    public void sort(Comparator comparator, boolean b) {
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
        clearData();
    }

}
