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
        String msg = "APP: " + getTrackExecutionState().getInstanceId() + " || " + message;
        getLogger().info(msg);
        getTrackExecutionState().getLogging().addMessage(getTrackExecutionState().getInstanceId(), message);
        return true;
    }
}
