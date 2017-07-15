package at.sintrum.fog.application.core.api.dto;

/**
 * Created by Michael Mittermayr on 15.07.2017.
 */
public class AppInfo {

    private boolean requiresUpdate;

    private String activeProfiles;

    private String metadataId;

    private String message;

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

    public String getActiveProfiles() {
        return activeProfiles;
    }

    public void setActiveProfiles(String activeProfiles) {
        this.activeProfiles = activeProfiles;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
