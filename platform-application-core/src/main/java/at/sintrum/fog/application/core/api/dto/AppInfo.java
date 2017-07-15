package at.sintrum.fog.application.core.api.dto;

/**
 * Created by Michael Mittermayr on 15.07.2017.
 */
public class AppInfo {

    private boolean requiresUpdate;

    private String metadataId;

    public boolean isRequiresUpdate() {
        return requiresUpdate;
    }

    public void setRequiresUpdate(boolean requiresUpdate) {
        this.requiresUpdate = requiresUpdate;
    }

    public String getMetadataId() {
        return metadataId;
    }

    public void setMetadataId(String metadataId) {
        this.metadataId = metadataId;
    }
}
