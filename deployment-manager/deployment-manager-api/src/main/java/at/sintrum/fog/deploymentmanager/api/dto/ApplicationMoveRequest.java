package at.sintrum.fog.deploymentmanager.api.dto;

/**
 * Created by Michael Mittermayr on 03.06.2017.
 */
public class ApplicationMoveRequest {

    private String containerId;

    private String targetFog;

    private String applicationUrl;

    private boolean standbyMode;

    private boolean upgradeApplicationIfPossible;

    public ApplicationMoveRequest(String containerId, String targetFog) {
        this.containerId = containerId;
        this.targetFog = targetFog;
    }

    public ApplicationMoveRequest() {
    }

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

    public String getApplicationUrl() {
        return applicationUrl;
    }

    public void setApplicationUrl(String applicationUrl) {
        this.applicationUrl = applicationUrl;
    }

    public boolean isStandbyMode() {
        return standbyMode;
    }

    public void setStandbyMode(boolean standbyMode) {
        this.standbyMode = standbyMode;
    }

    public boolean isUpgradeApplicationIfPossible() {
        return upgradeApplicationIfPossible;
    }

    public void setUpgradeApplicationIfPossible(boolean upgradeApplicationIfPossible) {
        this.upgradeApplicationIfPossible = upgradeApplicationIfPossible;
    }
}
