package at.sintrum.fog.simulation.simulation;

import at.sintrum.fog.core.dto.FogIdentification;
import org.joda.time.DateTime;

/**
 * Created by Michael Mittermayr on 09.09.2017.
 */
public class AppEvent {

    private String eventType;

    private DateTime time;

    private String note;

    private FogIdentification location;

    public AppEvent() {
    }

    public AppEvent(String eventType, DateTime time, FogIdentification location) {
        this.eventType = eventType;
        this.time = time;
        this.location = location;
    }

    public AppEvent(String eventType, DateTime time, FogIdentification location, String note) {
        this.eventType = eventType;
        this.time = time;
        this.location = location;
        this.note = note;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public DateTime getTime() {
        return time;
    }

    public void setTime(DateTime time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public FogIdentification getLocation() {
        return location;
    }

    public void setLocation(FogIdentification location) {
        this.location = location;
    }
}
