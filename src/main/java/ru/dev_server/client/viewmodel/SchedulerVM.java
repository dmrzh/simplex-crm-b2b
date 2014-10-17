package ru.dev_server.client.viewmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.sys.BinderCtrl;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import ru.dev_server.client.AuthService;
import ru.dev_server.client.dao.EmployeeDAO;
import ru.dev_server.client.model.Employee;

import java.util.HashMap;

/**. */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class SchedulerVM {
    private static final Logger LOG= LoggerFactory.getLogger(SchedulerVM.class);

    @WireVariable
    private AuthService authService;
    @WireVariable
    private EmployeeDAO employeeDAO;

    private String days="7";

    @Init
    public void init(){
       showMyMeetings= authService.getEmployee().isShowOnlyMyMeetings();
    }


    @GlobalCommand
    public void refreshMeetings(){
        BindUtils.postGlobalCommand("meetingQueue", null,"refreshMeetings",new HashMap<String, Object>());
    }

    @Command
    public void scale(Event event) {

        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("days",Integer.parseInt(days));
        BindUtils.postGlobalCommand(BinderCtrl.DEFAULT_QUEUE_NAME, null,"switchScale", args);
    }

    @Command() @NotifyChange("showMyMeetings")
    public void changeVisibility(){
        Employee employee = authService.getEmployee();
        showMyMeetings = !showMyMeetings;
        employee.setShowOnlyMyMeetings(showMyMeetings);
        employeeDAO.saveOrUpdate(employee);
    }


    private boolean showMyMeetings;

    public boolean isShowMyMeetings() {
        return showMyMeetings;
    }

    public void setShowMyMeetings(boolean showMyMeetings) {
        this.showMyMeetings = showMyMeetings;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }
}
