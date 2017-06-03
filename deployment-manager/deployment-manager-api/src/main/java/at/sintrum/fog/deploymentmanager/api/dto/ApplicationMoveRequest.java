package at.sintrum.fog.deploymentmanager.api.dto;

/**
 * Created by Michael Mittermayr on 03.06.2017.
 */
public class ApplicationMoveRequest {

    private String containerId;

    private String targetFog;

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getTargetFog() {
        return targetFog;
    }

    public void setTargetFog(String targetFog) {
        this.targetFog = targetFog;
    }
}
