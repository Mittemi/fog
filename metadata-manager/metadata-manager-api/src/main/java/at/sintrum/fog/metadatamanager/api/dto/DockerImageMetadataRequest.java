package at.sintrum.fog.metadatamanager.api.dto;

/**
 * Created by Michael Mittermayr on 31.05.2017.
 */
public class DockerImageMetadataRequest {

    public DockerImageMetadataRequest(String imageId) {
        this.imageId = imageId;
    }

    public DockerImageMetadataRequest() {
    }

    private String imageId;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}
