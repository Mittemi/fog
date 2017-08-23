package at.sintrum.fog.applicationhousing.recovery.metadata;

import org.joda.time.DateTime;
import org.joda.time.Seconds;

/**
 * Created by Michael Mittermayr on 23.08.2017.
 */
public class RuntimeMetadataBase {
    private final String serviceId;

    private DateTime timeAdded;
    private DateTime lastTimeActive;

    RuntimeMetadataBase(String serviceId) {
        this.serviceId = serviceId;
        timeAdded = new DateTime();
    }

    public DateTime getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(DateTime timeAdded) {
        this.timeAdded = timeAdded;
    }

    public DateTime getLastTimeActive() {
        return lastTimeActive;
    }

    public void setLastTimeActive(DateTime lastTimeActive) {
        this.lastTimeActive = lastTimeActive;
    }

    public void heartbeat() {
        lastTimeActive = new DateTime();
    }

    public String getServiceId() {
        return serviceId;
    }

    public boolean hasTimeout() {
        return getLastTimeActive() == null || Seconds.secondsBetween(getLastTimeActive(), new DateTime()).isGreaterThan(Seconds.seconds(60));
    }
}