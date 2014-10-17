package ru.dev_server.client.viewmodel;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.GlobalCommandEvent;
import org.zkoss.bind.sys.BinderCtrl;
import org.zkoss.calendar.Calendars;
import org.zkoss.calendar.event.CalendarsEvent;
import org.zkoss.calendar.impl.SimpleCalendarModel;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import ru.dev_server.client.AuthService;
import ru.dev_server.client.dao.itevents.EventDAO;
import ru.dev_server.client.model.Company;
import ru.dev_server.client.model.Employee;
import ru.dev_server.client.model.ItEvent;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**. */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class CalendarController extends SelectorComposer {
    private static final Logger LOG= LoggerFactory.getLogger(CalendarController.class);

    @WireVariable
    private EventDAO itEventDAO;

    @WireVariable
    private AuthService authService;


    private Calendars cals;

    public Employee employee;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        cals = (Calendars) comp;


        EventQueues.lookup(BinderCtrl.DEFAULT_QUEUE_NAME, EventQueues.DESKTOP, true).subscribe(
                new EventListener<Event>() {
                    public void onEvent(Event evt) {
                        back(evt);
                        forward(evt);
                    }
                }
        );


        EventQueues.lookup(BinderCtrl.DEFAULT_QUEUE_NAME, EventQueues.DESKTOP, true).subscribe(
                new EventListener<Event>() {
                    public void onEvent(Event evt) {
                        refreshMeetings(evt);
                        refreshWorkTime(evt);
                    }
                });

        EventQueues.lookup(BinderCtrl.DEFAULT_QUEUE_NAME, EventQueues.DESKTOP, true).subscribe(
                new EventListener<Event>() {
                    public void onEvent(Event evt) {
                        switchScale(evt);
                        setOwner(evt);
                    }
                });

        setWorkTime();
        super.doAfterCompose(cals);
        employee=authService.getEmployee();
        refresh();
    }

    private void setWorkTime() {
        Company company = authService.getCompany();
        cals.setBeginTime(company.getStartWorkingHours());
        cals.setEndTime(company.getFinishWorkingHours());
        cals.setTimeZone(company.getTimezone());
    }

    private void refresh() {
        SimpleCalendarModel model = new SimpleCalendarModel();
        List<ItEvent> all;
        Date end = new LocalDate(cals.getCurrentDate().getTime()).plusDays(7).toDate();
        Date start = new LocalDate(cals.getCurrentDate().getTime()).minusDays(7).toDate();


        all = itEventDAO.findOwned(employee,start,end);

        for(ItEvent m:all){
            model.add(new MeetingEvent(m) );
        }
        cals.setModel(model);;
    }



    public void switchScale(Event evt){
        if(evt instanceof GlobalCommandEvent){
            GlobalCommandEvent GlobalCmdEv = (GlobalCommandEvent) evt;
            if("switchScale".equals(GlobalCmdEv.getCommand())){
                Integer days = (Integer)GlobalCmdEv.getArgs().get("days");
                cals.setDays(days);
                if(days.equals(30)){
                    cals.setMold("month");
                }else{
                    cals.setMold("default");
                }
            }
        }
    }

    public void setOwner(Event evt){
        if(evt instanceof GlobalCommandEvent){
            GlobalCommandEvent GlobalCmdEv = (GlobalCommandEvent) evt;
            if("setOwner".equals(GlobalCmdEv.getCommand())){
                  employee=(Employee)GlobalCmdEv.getArgs().get("owner");
                  if(employee==null){
                      employee=authService.getEmployee();
                  }
                  refresh();
            }
        }
    }

    public void refreshMeetings(Event evt){
        if(evt instanceof GlobalCommandEvent){
            GlobalCommandEvent GlobalCmdEv = (GlobalCommandEvent) evt;
            if("refreshMeetings".equals(GlobalCmdEv.getCommand())){
                refresh();
            }
        }
    }
    public void refreshWorkTime(Event evt){
        if(evt instanceof GlobalCommandEvent){
            GlobalCommandEvent GlobalCmdEv = (GlobalCommandEvent) evt;
            if("refreshWorkTime".equals(GlobalCmdEv.getCommand())){
                setWorkTime();
            }
        }
    }

    ///@Subscribe(BinderCtrl.DEFAULT_QUEUE_NAME)  //need zkoss EE
    public void back(Event evt){
        if(evt instanceof GlobalCommandEvent){
            GlobalCommandEvent GlobalCmdEv = (GlobalCommandEvent) evt;
            if("backCalendar".equals(GlobalCmdEv.getCommand())){
                cals.setCurrentDate(new LocalDate(cals.getCurrentDate().getTime()).minusDays(cals.getDays()).toDate());
                refresh();
            }
        }
    }
    //@Subscribe(BinderCtrl.DEFAULT_QUEUE_NAME)  //need zkoss EE
    public void forward(Event evt){
        if(evt instanceof GlobalCommandEvent){
            GlobalCommandEvent GlobalCmdEv = (GlobalCommandEvent) evt;
            if("forwardCalendar".equals(GlobalCmdEv.getCommand())){
                cals.setCurrentDate(new LocalDate(cals.getCurrentDate().getTime()).plusDays(cals.getDays()).toDate());
                refresh();
            }
        }
    }

    @Listen("onEventCreate = calendars")
    public void onEventCreate(CalendarsEvent event){
//        LOG.debug("CalendarsEvent EventCreate");
//        Employee employee = authService.getEmployee();
//        Meeting meeting =dynamicaColumnUtils.createMeeting();
//        meeting.setBeginDate(event.getBeginDate());
//        meeting.setEndDate(event.getEndDate());
//
//        meeting.setEmployee(employee);
//        meeting.setSmsText(employee.getCompany().getDefaultSmsText());
//        meeting.setRememberBeforeHours(employee.getCompany().getDefaultReminderTime());
//        HashMap params = new HashMap();
//        params.put("meeting",meeting);
//        Executions. createComponents("/meeting.zul",null,params );
    }
    @Listen("onEventEdit = calendars")
    public void onEventEdit(CalendarsEvent event){
        LOG.debug("CalendarsEvent EventEdit");
        MeetingEvent calendarEvent = (MeetingEvent)event.getCalendarEvent();
        ItEvent itEvent= calendarEvent.getItEvent();


        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("juridicalPerson", itEvent.getClient());
        Executions. createComponents("/WEB-INF/zul/juridicalPerson.zul", null, args);

    }
//    @Listen("onEventUpdate = calendars")
//    public void onEventUpdate(CalendarsEvent event){
//        LOG.debug("CalendarsEvent onEventUpdate");
//        MeetingEvent calendarEvent = (MeetingEvent)event.getCalendarEvent();
//        Meeting meeting = calendarEvent.getMeeting();
//        meeting=meetingDAO.get(meeting.getId());
//        meeting.setBeginDate(event.getBeginDate());
//        meeting.setEndDate(event.getEndDate());
//        HashMap params = new HashMap();
//        params.put("meeting",meeting);
//        Executions. createComponents("/meeting.zul",null,params );
//    }




}
