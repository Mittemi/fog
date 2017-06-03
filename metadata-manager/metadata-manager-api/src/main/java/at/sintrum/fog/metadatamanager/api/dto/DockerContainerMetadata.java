package at.sintrum.fog.metadatamanager.api.dto;

/**
 * Created by Michael Mittermayr on 03.06.2017.
 */
public class DockerContainerMetadata extends MetadataBase {

    private String containerId;

    private String imageMetadataId;

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getImageMetadataId() {
        return imageMetadataId;
    }

    public void setImageMetadataId(String imageMetadataId) {
        this.imageMetadataId = imageMetadataId;
    }
}
