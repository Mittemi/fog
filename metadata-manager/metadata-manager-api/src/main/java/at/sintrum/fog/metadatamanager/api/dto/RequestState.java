package at.sintrum.fog.metadatamanager.api.dto;

import org.joda.time.DateTime;

/**
 * Created by Michael Mittermayr on 30.10.2017.
 */
public class RequestState {

    private String internalId;

    private int credits;

    private DateTime creationDate;

    private boolean finished;

    public RequestState() {
    }

    public RequestState(String internalId, int credits, DateTime creationDate, boolean finished) {
        this.internalId = internalId;
        this.credits = credits;
        this.creationDate = creationDate;
        this.finished = finished;
    }

    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public DateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(DateTime creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
