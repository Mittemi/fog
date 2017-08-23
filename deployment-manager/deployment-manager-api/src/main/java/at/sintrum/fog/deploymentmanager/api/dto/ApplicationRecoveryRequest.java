package at.sintrum.fog.deploymentmanager.api.dto;

/**
 * Created by Michael Mittermayr on 23.08.2017.
 */
public class ApplicationRecoveryRequest {

    private String instanceId;

    public ApplicationRecoveryRequest(String instanceId) {
        this.instanceId = instanceId;
    }

    public ApplicationRecoveryRequest() {
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
