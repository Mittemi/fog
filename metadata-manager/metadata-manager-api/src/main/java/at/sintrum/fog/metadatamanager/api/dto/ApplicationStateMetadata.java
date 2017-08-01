package at.sintrum.fog.metadatamanager.api.dto;

import at.sintrum.fog.core.dto.FogIdentification;

/**
 * Created by Michael Mittermayr on 01.08.2017.
 */
public class ApplicationStateMetadata extends MetadataBase {

    private String instanceId;
    private FogIdentification runningAt;
    private FogIdentification nextTarget;
    private AppState state;

    public ApplicationStateMetadata() {
    }

    public ApplicationStateMetadata(String instanceId) {
        this.instanceId = instanceId;
    }

    public ApplicationStateMetadata(String instanceId, FogIdentification runningAt, AppState state) {
        this.instanceId = instanceId;
        this.runningAt = runningAt;
        this.state = state;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public FogIdentification getRunningAt() {
        return runningAt;
    }

    public void setRunningAt(FogIdentification runningAt) {
        this.runningAt = runningAt;
    }

    public AppState getState() {
        return state;
    }

    public void setState(AppState state) {
        this.state = state;
    }

    public FogIdentification getNextTarget() {
        return nextTarget;
    }

    public void setNextTarget(FogIdentification nextTarget) {
        this.nextTarget = nextTarget;
    }
}
