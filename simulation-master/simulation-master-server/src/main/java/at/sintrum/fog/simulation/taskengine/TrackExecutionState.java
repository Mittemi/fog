package at.sintrum.fog.simulation.taskengine;

import at.sintrum.fog.simulation.taskengine.log.ExecutionLogging;

/**
 * Created by Michael Mittermayr on 09.09.2017.
 */
public class TrackExecutionState {
    private String instanceId;
    private int currentTaskIndex;

    private final ExecutionLogging logging;

    public TrackExecutionState(String instanceId) {
        this.instanceId = instanceId;
        logging = new ExecutionLogging();
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public ExecutionLogging getLogging() {
        return logging;
    }

    public int getCurrentTaskIndex() {
        return currentTaskIndex;
    }

    public void setCurrentTaskIndex(int currentTaskIndex) {
        this.currentTaskIndex = currentTaskIndex;
    }

    public void taskFinished() {
        currentTaskIndex++;
    }
}
