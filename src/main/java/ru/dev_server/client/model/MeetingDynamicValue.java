package ru.dev_server.client.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**.*/
@Entity
public class MeetingDynamicValue extends DynamicValue {
    @ManyToOne
    private Meeting meeting;

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

}
