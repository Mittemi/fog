package at.sintrum.fog.application.core.model;

import at.sintrum.fog.core.dto.FogIdentification;
import org.joda.time.DateTime;

/**
 * Created by Michael Mittermayr on 12.10.2017.
 */
public class AppRequestInfo {

    private FogIdentification target;

    private DateTime requestDate;

    private int estimatedDuration;

    public DateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(DateTime requestDate) {
        this.requestDate = requestDate;
    }

    public int getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(int estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public AppRequestInfo(FogIdentification target) {
        this.target = target;
    }

    public AppRequestInfo() {
    }

    public AppRequestInfo(FogIdentification target, DateTime requestDate, int estimatedDuration) {
        this.target = target;
        this.requestDate = requestDate;
        this.estimatedDuration = estimatedDuration;
    }

    public FogIdentification getTarget() {
        return target;
    }

    public void setTarget(FogIdentification target) {
        this.target = target;
    }
}
