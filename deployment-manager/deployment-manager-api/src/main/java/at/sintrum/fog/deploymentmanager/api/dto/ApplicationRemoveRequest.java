package at.sintrum.fog.deploymentmanager.api.dto;

/**
 * Created by Michael Mittermayr on 28.08.2017.
 */
public class ApplicationRemoveRequest {

    private String containerId;

    private String applicationUrl;

    public ApplicationRemoveRequest() {
    }

    public ApplicationRemoveRequest(String containerId, String applicationUrl) {
        this.containerId = containerId;
        this.applicationUrl = applicationUrl;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getApplicationUrl() {
        return applicationUrl;
    }

    public void setApplicationUrl(String applicationUrl) {
        this.applicationUrl = applicationUrl;
    }
}
