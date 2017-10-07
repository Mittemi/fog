package at.sintrum.fog.simulation.simulation;

import java.util.LinkedList;

/**
 * Created by Michael Mittermayr on 09.09.2017.
 */
public class AppExecutionLogging {

    private String imageMetadataId;

    private LinkedList<AppEvent> events = new LinkedList<>();

    public AppExecutionLogging(String imageMetadataId) {
        this.imageMetadataId = imageMetadataId;
    }

    public LinkedList<AppEvent> getEvents() {
        return events;
    }

    public void setEvents(LinkedList<AppEvent> events) {
        this.events = events;
    }

    public String getImageMetadataId() {
        return imageMetadataId;
    }

    public void setImageMetadataId(String imageMetadataId) {
        this.imageMetadataId = imageMetadataId;
    }
}
