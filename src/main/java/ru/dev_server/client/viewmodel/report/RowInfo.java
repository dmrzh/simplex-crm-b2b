package ru.dev_server.client.viewmodel.report;

import ru.dev_server.client.model.ItEvent;

/**
 * .
 */
public class RowInfo {
    private String name="";
    private ItEvent.EventType eventType;
    private String publicProup="";
    private String privateProup="";
    private String calls="";
    private String meetings="";
    private String rub="";
    private Double euro;

    private Object coreObject;

    public RowInfo() {
    }

    public RowInfo(String publicProup, String privateProup, String calls, String meetings, String rub, Double euro) {
        this.publicProup = publicProup;
        this.privateProup = privateProup;
        this.calls = calls;
        this.meetings = meetings;
        this.rub = rub;
        this.euro = euro;
    }

    public String getPublicProup() {
        return publicProup;
    }

    public void setPublicProup(String publicProup) {
        this.publicProup = publicProup;
    }

    public String getPrivateProup() {
        return privateProup;
    }

    public void setPrivateProup(String privateProup) {
        this.privateProup = privateProup;
    }

    public String getCalls() {
        return calls;
    }

    public void setCalls(String calls) {
        this.calls = calls;
    }

    public String getMeetings() {
        return meetings;
    }

    public void setMeetings(String meetings) {
        this.meetings = meetings;
    }

    public String getRub() {
        return rub;
    }

    public void setRub(String rub) {
        this.rub = rub;
    }

    public Double  getEuro() {
        return euro;
    }

    public void setEuro(Double  euro) {
        this.euro = euro;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItEvent.EventType getEventType() {
        return eventType;
    }

    public void setEventType(ItEvent.EventType eventType) {
        this.eventType = eventType;
    }

    public Object getCoreObject() {
        return coreObject;
    }

    public void setCoreObject(Object coreObject) {
        this.coreObject = coreObject;
    }
}
