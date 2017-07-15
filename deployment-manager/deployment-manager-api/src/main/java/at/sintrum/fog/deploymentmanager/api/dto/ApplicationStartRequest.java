package at.sintrum.fog.deploymentmanager.api.dto;

/**
 * Created by Michael Mittermayr on 31.05.2017.
 */
public class ApplicationStartRequest {

    private String metadataId;

    private boolean skipPull;

    private boolean standbyMode;

    private boolean upgradeApplicationIfPossible;

    public ApplicationStartRequest(String metadataId) {
        this.metadataId = metadataId;
    }

    public ApplicationStartRequest() {
    }

    public String getMetadataId() {
        return metadataId;
    }

    public void setMetadataId(String metadataId) {
        this.metadataId = metadataId;
    }

    public boolean isSkipPull() {
        return skipPull;
    }

    public void setSkipPull(boolean skipPull) {
        this.skipPull = skipPull;
    }

    public boolean isUpgradeApplicationIfPossible() {
        return upgradeApplicationIfPossible;
    }

    public void setUpgradeApplicationIfPossible(boolean upgradeApplicationIfPossible) {
        this.upgradeApplicationIfPossible = upgradeApplicationIfPossible;
    }

    public boolean isStandbyMode() {
        return standbyMode;
    }

    public void setStandbyMode(boolean standbyMode) {
        this.standbyMode = standbyMode;
    }
}
