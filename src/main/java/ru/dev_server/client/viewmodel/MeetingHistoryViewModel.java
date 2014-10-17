package ru.dev_server.client.viewmodel;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;
import ru.dev_server.client.AuthService;
import ru.dev_server.client.dao.MeetingDAO;
import ru.dev_server.client.model.Client;
import ru.dev_server.client.model.Meeting;

import java.util.ArrayList;
import java.util.List;

/**.*/
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class MeetingHistoryViewModel {
    private Window meetingHistoryWin;
    private Client currentClient;
    @WireVariable("authService")
    private AuthService authService;
    @WireVariable("meetingDAO")
    private MeetingDAO meetingDAO;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view,@ExecutionArgParam("client") Client client){
        meetingHistoryWin =view;
        currentClient=client;
        if(client.getId()!=null){
            meetingList=meetingDAO.findByClient(client);
        }
    }
    private List<Meeting> meetingList=new ArrayList<Meeting>();

    public List<Meeting> getMeetingList() {
        return meetingList;
    }

    public void setMeetingList(List<Meeting> meetingList) {
        this.meetingList = meetingList;
    }
}
