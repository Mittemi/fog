package at.sintrum.fog.metadatamanager.api.dto;

import at.sintrum.fog.core.dto.FogIdentification;

/**
 * Created by Michael Mittermayr on 12.10.2017.
 */
public class AppRequest {

    private FogIdentification target;

    private String instanceId;

    private int estimatedDuration;

    public AppRequest(FogIdentification target, String instanceId, int estimatedDuration) {
        this.target = target;
        this.instanceId = instanceId;
        this.estimatedDuration = estimatedDuration;
    }

    public int getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(int estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public AppRequest(FogIdentification target) {
        this.target = target;
    }

    public AppRequest() {
    }

    public FogIdentification getTarget() {
        return target;
    }

    public void setTarget(FogIdentification target) {
        this.target = target;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
