package at.sintrum.fog.deploymentmanager.api.dto;

import at.sintrum.fog.core.dto.FogIdentification;

/**
 * Created by Michael Mittermayr on 03.06.2017.
 */
public class ApplicationMoveRequest {

    private String containerId;

    private FogIdentification targetFog;

    private String applicationUrl;

    public ApplicationMoveRequest(String containerId, FogIdentification targetFog, String applicationUrl) {
        this.containerId = containerId;
        this.targetFog = targetFog;
        this.applicationUrl = applicationUrl;
    }

    public ApplicationMoveRequest() {
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public FogIdentification getTargetFog() {
        return targetFog;
    }

    public void setTargetFog(FogIdentification targetFog) {
        this.targetFog = targetFog;
    }

    public String getApplicationUrl() {
        return applicationUrl;
    }

    public void setApplicationUrl(String applicationUrl) {
        this.applicationUrl = applicationUrl;
    }
}
