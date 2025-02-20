package at.sintrum.fog.metadatamanager.api.dto;

/**
 * Created by Michael Mittermayr on 03.06.2017.
 */
public class DockerContainerMetadata extends MetadataBase {

    public DockerContainerMetadata(String containerId, String imageMetadataId, String fogId, String instanceId) {
        this.containerId = containerId;
        this.imageMetadataId = imageMetadataId;
        this.fogId = fogId;
        this.instanceId = instanceId;
    }

    public DockerContainerMetadata() {

    }

    private String containerId;

    private String imageMetadataId;

    private String instanceId;

    private String fogId;

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

    public String getFogId() {
        return fogId;
    }

    public void setFogId(String fogId) {
        this.fogId = fogId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
