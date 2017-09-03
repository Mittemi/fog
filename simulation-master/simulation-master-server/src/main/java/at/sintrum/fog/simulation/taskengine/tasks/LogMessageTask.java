package at.sintrum.fog.simulation.taskengine.tasks;

import at.sintrum.fog.simulation.taskengine.TaskListBuilder;

/**
 * Created by Michael Mittermayr on 24.08.2017.
 */
public class LogMessageTask extends FogTaskBase {

    private final String message;

    public LogMessageTask(int offset, String message, TaskListBuilder.TaskListBuilderState.AppTaskBuilder.TrackExecutionState trackExecutionState) {
        super(offset, trackExecutionState, LogMessageTask.class);
        this.message = message;
    }

    @Override
    protected boolean internalExecute() {
        getLogger().info("APP: " + getTrackExecutionState().getInstanceId() + " || " + message);
        return true;
    }
}
