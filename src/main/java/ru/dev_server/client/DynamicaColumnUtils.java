package ru.dev_server.client;

import ru.dev_server.client.dao.ClientDAO;
import ru.dev_server.client.dao.ClientDynamicValueDAO;
import ru.dev_server.client.dao.DynamicColumnDAO;
import ru.dev_server.client.dao.MeetingDAO;
import ru.dev_server.client.dao.MeetingDynamicValueDAO;
import ru.dev_server.client.model.Client;
import ru.dev_server.client.model.ClientDynamicValue;
import ru.dev_server.client.model.Company;
import ru.dev_server.client.model.DynamicColumn;
import ru.dev_server.client.model.ExstendedTables;
import ru.dev_server.client.model.Meeting;
import ru.dev_server.client.model.MeetingDynamicValue;

import javax.annotation.Resource;
import java.util.List;

/**.*/
public class DynamicaColumnUtils {
    @Resource
    private ClientDAO clientDAO;
    @Resource
    private MeetingDAO meetingDAO;

    @Resource
    private DynamicColumnDAO dynamicColumnDAO;

    @Resource
    private ClientDynamicValueDAO clientDynamicValueDAO;

    @Resource
    private MeetingDynamicValueDAO meetingDynamicValueDAO;

    @Resource
    private AuthService authService;


    public void updateDependentObjects(DynamicColumn dynamicColumn) {
        if(dynamicColumn.getExstendedTables()== ExstendedTables.CLIENT){
            List<Client> clients = clientDAO.findAll(authService.getCompany());
            for(Client c:clients){
                ClientDynamicValue dynamicValue = new ClientDynamicValue();
                dynamicValue.setClient(c);
                dynamicValue.setDynamicColumn(dynamicColumn);
                clientDynamicValueDAO.saveOrUpdate(dynamicValue);
                c.getDynamicValueList().put(dynamicColumn, dynamicValue) ;
            }
        }else if(dynamicColumn.getExstendedTables()==ExstendedTables.MEETING){
            List<Meeting> meetings = meetingDAO.findAll(authService.getCompany());
            for(Meeting m:meetings){
                MeetingDynamicValue dynamicValue = new MeetingDynamicValue();
                dynamicValue.setMeeting(m);
                dynamicValue.setDynamicColumn(dynamicColumn);
                meetingDynamicValueDAO.saveOrUpdate(dynamicValue);
                m.getDynamicValueList().put(dynamicColumn,dynamicValue);
            }
        }else{
            throw new IllegalStateException();
        }
    }
    public Meeting createMeeting(){
        Meeting meeting = new Meeting();
        List<DynamicColumn> dynamicColumnList = dynamicColumnDAO.findAll(authService.getCompany(), ExstendedTables.MEETING);
        for(DynamicColumn dynamicColumn:dynamicColumnList){
            MeetingDynamicValue meetingDynamicValue = new MeetingDynamicValue();
            meetingDynamicValue.setDynamicColumn(dynamicColumn);
            meetingDynamicValue.setMeeting(meeting);
            meeting.getDynamicValueList().put(dynamicColumn,meetingDynamicValue);
        }
        return  meeting;
    }
    public Client createClient(){
        Company company = authService.getCompany();
        Client c =new Client();
        c.setCompany(company);
        for(DynamicColumn dc:dynamicColumnDAO.findAll(company, ExstendedTables.CLIENT)){
            ClientDynamicValue value = new ClientDynamicValue();
            value.setClient(c);
            value.setDynamicColumn(dc);
            c.getDynamicValueList().put(dc,value);
        }
        return c;

    }
}
