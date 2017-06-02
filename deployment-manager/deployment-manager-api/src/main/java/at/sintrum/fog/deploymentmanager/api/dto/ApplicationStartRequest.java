package at.sintrum.fog.deploymentmanager.api.dto;

/**
 * Created by Michael Mittermayr on 31.05.2017.
 */
public class ApplicationStartRequest {

    private String metadataId;

    public String getMetadataId() {
        return metadataId;
    }

    public void setMetadataId(String metadataId) {
        this.metadataId = metadataId;
    }
}
