package at.sintrum.fog.applicationhousing.api.dto;

/**
 * Created by Michael Mittermayr on 15.07.2017.
 */
public class AppUpdateInfo {

    private boolean updateRequired;

    private String imageMetadataId;

    public boolean isUpdateRequired() {
        return updateRequired;
    }

    public void setUpdateRequired(boolean updateRequired) {
        this.updateRequired = updateRequired;
    }

    public String getImageMetadataId() {
        return imageMetadataId;
    }

    public void setImageMetadataId(String imageMetadataId) {
        this.imageMetadataId = imageMetadataId;
    }
}
