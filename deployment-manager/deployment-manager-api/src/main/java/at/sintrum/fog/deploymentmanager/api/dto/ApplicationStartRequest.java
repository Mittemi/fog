package at.sintrum.fog.deploymentmanager.api.dto;

/**
 * Created by Michael Mittermayr on 31.05.2017.
 */
public class ApplicationStartRequest {

    private String metadataId;

    private String instanceId;

    private boolean skipPull;

    public ApplicationStartRequest(String metadataId, String instanceId) {
        this.metadataId = metadataId;
        this.instanceId = instanceId;
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

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
