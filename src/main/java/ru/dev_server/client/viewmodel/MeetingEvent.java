package ru.dev_server.client.viewmodel;

import org.zkoss.calendar.api.CalendarEvent;
import org.zkoss.xml.XMLs;
import ru.dev_server.client.model.ItEvent;
import ru.dev_server.client.model.JuridicalPerson;

import java.util.Calendar;
import java.util.Date;

/**.*/
public class MeetingEvent implements CalendarEvent {
    public static final String TITLE_COLOR_LABEL = "simplex-crm.itEvent.title.color";
    private ItEvent itEvent;


    public MeetingEvent(ItEvent itEvent) {
        this.itEvent = itEvent;
    }

    @Override
    public Date getBeginDate() {
        return itEvent.getEventTime();
    }

    @Override
    public Date getEndDate() {
        Calendar instance = Calendar.getInstance();
        instance.setTime(itEvent.getEventTime());
        instance.add(Calendar.HOUR, 1);
        return instance.getTime();
    }

    @Override
    public String getTitle() {
        if(itEvent.getClient()==null){
            return "";
        }
        JuridicalPerson client = (JuridicalPerson) itEvent.getClient();
        String fio = XMLs.escapeXML(client.getName());
        return fio;
    }

    @Override
    public String getContent() {
        return XMLs.escapeXML(itEvent.getText());
    }

    @Override
    public String getHeaderColor() {
        if(!itEvent.isCompleted() && itEvent.isRemind() && itEvent.getEventTime().before(new Date())){
            return "red";
        }
        return "";
    }

    @Override
    public String getContentColor() {
        return "";
    }

    @Override
    public String getZclass() {
        return "z-calevent";
    }

    @Override
    public boolean isLocked() {
        return false;
    }

    public ItEvent getItEvent() {
        return itEvent;
    }
}
