package ru.dev_server.client.viewmodel;

import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zul.AbstractListModel;
import org.zkoss.zul.event.ListDataEvent;
import ru.dev_server.client.AuthService;
import ru.dev_server.client.dao.JuridicalOldFilterDAO;
import ru.dev_server.client.dao.JuridicalPersonDAO;
import ru.dev_server.client.dao.itevents.ItGroupDAO;
import ru.dev_server.client.model.Company;
import ru.dev_server.client.model.ItGroup;
import ru.dev_server.client.model.JuridicalPerson;
import ru.dev_server.client.model.JuridicalPersonFilter;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * .
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class JuridicalPersonListModel extends AbstractListModel<JuridicalPerson > implements org.zkoss.zul.ext.Sortable {
    private Long[] clientIds;

    private JuridicalPersonDAO juridicalPersonDAO;


    private List<ItGroup> allPrivateGroups;
    private List<ItGroup> allPublicGroups;

    private ItGroupDAO itGroupDAO;
    private JuridicalOldFilterDAO juridicalOldFilterDAO;


    private AuthService authService;

    private JuridicalPersonFilter filter;
    private int PAGE_SIZE = 50;

    public JuridicalPersonListModel(JuridicalPersonDAO juridicalPersonDAO,AuthService authService,  ItGroupDAO itGroupDAO,JuridicalOldFilterDAO juridicalOldFilterDAO) {
        this.juridicalOldFilterDAO=juridicalOldFilterDAO;
        this.juridicalPersonDAO= juridicalPersonDAO;
        this.itGroupDAO=itGroupDAO;
        this.authService = authService;
        clientIds =new Long[0];
        clearData();
        allPublicGroups=itGroupDAO.findPublic(authService.getCompany());
        allPrivateGroups=itGroupDAO.findPrivate(authService.getEmployee());
    }


    public void clearData(){
        clearSelection();
        long clientCount;
        if(filter==null) {
            clientCount= juridicalPersonDAO.getClientCount(authService.getCompany());
        }else{



            List<ItGroup> noPrivGroup=new LinkedList<ItGroup>(allPrivateGroups);
            noPrivGroup.removeAll(filter.getSelectedPrivateGroups());

            List<ItGroup> noPubGroup=new LinkedList<ItGroup>(allPublicGroups);
            noPubGroup.removeAll(filter.getSelectedPublicGroups());


            clientCount= juridicalOldFilterDAO.getJuridicalPersonCountByFilter(filter,
                    noPubGroup,  !filter.getSelectedPublicGroups().isEmpty(),
                    noPrivGroup,  !filter.getSelectedPrivateGroups().isEmpty(),
                    authService.getEmployee(),
                    authService.getCompany());
        }
        int oldClientCount= clientIds.length;
        clientIds =new Long[((int)clientCount)];
//        if(oldClientCount>0){
            fireEvent(ListDataEvent.CONTENTS_CHANGED, -1, -1);

//        }
    }

    public void refreshSelection(int index ){
        fireEvent(ListDataEvent.SELECTION_CHANGED,index,index);
    }

    public void  selectPerson(JuridicalPerson juridicalPerson) {
        for(int i=0;i< clientIds.length;i++){
            if(clientIds[i]==null){
                getElementAt(i);
            }
            if(juridicalPerson.getId()==clientIds[i]){
                fireEvent(ListDataEvent.SELECTION_CHANGED, i,i);
                return;
            }
        }
        int indexLast=clientIds.length-1;
        fireEvent(ListDataEvent.SELECTION_CHANGED, indexLast,indexLast);
    }

    @Override
    public JuridicalPerson  getElementAt(int index) {
        if(clientIds[index]==null){
            int i=0;
            for(JuridicalPerson c:getPageByIndex(index)){
                clientIds[index+(i++)]=c.getId();
            }
        }
        JuridicalPerson client = juridicalPersonDAO.get(clientIds[index]);
        return client;
    }

    private List<JuridicalPerson> getPageByIndex(int index) {
        List<JuridicalPerson> all;
        Company company = authService.getCompany();

        if(filter==null ){
            all = juridicalPersonDAO.findAll(company, index, PAGE_SIZE);
        }else{
            List<ItGroup> noPrivGroup=new LinkedList<ItGroup>(allPrivateGroups);
            noPrivGroup.removeAll(filter.getSelectedPrivateGroups());

            List<ItGroup> noPubGroup=new LinkedList<ItGroup>(allPublicGroups);
            noPubGroup.removeAll(filter.getSelectedPublicGroups());

            all=juridicalOldFilterDAO.findByFilter(filter,
                    noPubGroup,  !filter.getSelectedPublicGroups().isEmpty(),
                    noPrivGroup,  !filter.getSelectedPrivateGroups().isEmpty(),
                    authService.getEmployee(),
                    authService.getCompany(),index,PAGE_SIZE);
        }
        return all;
    }

    @Override
    public int getSize() {
        return clientIds.length;
    }

    @Override
    public void sort(Comparator cmpr, boolean ascending) {

    }

    @Override
    public String getSortDirection(Comparator cmpr) {
        return null;
    }

    public JuridicalPersonFilter getFilter() {
        return filter;
    }

    public void setFilter(JuridicalPersonFilter filter) {
        this.filter = filter;
    }
}
