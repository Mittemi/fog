package at.sintrum.fog.applicationhousing.api.dto;

/**
 * Created by Michael Mittermayr on 15.07.2017.
 */
public class AppIdentification {

    public AppIdentification() {
    }

    public AppIdentification(String imageMetadataId) {
        this.imageMetadataId = imageMetadataId;
    }

    private String imageMetadataId;

    public String getImageMetadataId() {
        return imageMetadataId;
    }

    public void setImageMetadataId(String imageMetadataId) {
        this.imageMetadataId = imageMetadataId;
    }
}
