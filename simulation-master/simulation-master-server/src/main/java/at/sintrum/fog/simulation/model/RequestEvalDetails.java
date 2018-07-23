package at.sintrum.fog.simulation.model;

/**
 * Created by Michael Mittermayr on 15.11.2017.
 */
public class RequestEvalDetails {

    private String fogId;

    private String id;

    private int credits;

    private int waitTime;

    private int timeAfterSimulationStart;

    private String instanceId;

    public RequestEvalDetails(String id, String fogId, int credits, int waitTime, int timeAfterSimulationStart, String instanceId) {
        this.id = id;
        this.fogId = fogId;
        this.credits = credits;
        this.waitTime = waitTime;
        this.timeAfterSimulationStart = timeAfterSimulationStart;
        this.instanceId = instanceId;
    }

    public String getFogId() {
        return fogId;
    }

    public void setFogId(String fogId) {
        this.fogId = fogId;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTimeAfterSimulationStart() {
        return timeAfterSimulationStart;
    }

    public void setTimeAfterSimulationStart(int timeAfterSimulationStart) {
        this.timeAfterSimulationStart = timeAfterSimulationStart;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String intanceId) {
        this.instanceId = intanceId;
    }
}
