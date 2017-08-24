package at.sintrum.fog.deploymentmanager.api.dto;

/**
 * Created by Michael Mittermayr on 06.06.2017.
 */
public class FogOperationResult {

    private String containerId;

    private String instanceId;

    private boolean successful;

    private String fogUrl;

    private String message;

    public FogOperationResult(String containerId, boolean successful, String fogUrl) {
        this.containerId = containerId;
        this.successful = successful;
        this.fogUrl = fogUrl;
    }

    public FogOperationResult(String containerId, boolean successful, String fogUrl, String message) {
        this.containerId = containerId;
        this.successful = successful;
        this.fogUrl = fogUrl;
        this.message = message;
    }

    public FogOperationResult() {
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public String getFogUrl() {
        return fogUrl;
    }

    public void setFogUrl(String fogUrl) {
        this.fogUrl = fogUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
