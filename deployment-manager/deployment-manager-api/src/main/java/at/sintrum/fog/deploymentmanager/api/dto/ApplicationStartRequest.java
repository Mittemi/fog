package at.sintrum.fog.deploymentmanager.api.dto;

/**
 * Created by Michael Mittermayr on 31.05.2017.
 */
public class ApplicationStartRequest {

    private String metadataId;

    private boolean skipPull;

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
}
