package at.sintrum.fog.simulation.taskengine;

/**
 * Created by Michael Mittermayr on 07.09.2017.
 */
public class TrackBuilderState {

    private String instanceId;
    private String containerId;

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
