package at.sintrum.fog.simulation.scenario.dto;

import at.sintrum.fog.simulation.taskengine.log.ExecutionLogging;

import java.util.List;

/**
 * Created by Michael Mittermayr on 09.09.2017.
 */
public class TrackExecutionInfo {

    private final int trackId;
    private final boolean finished;
    private final List<ExecutionLogging.LogEntry> logs;
    private final int currentTaskIndex;

    public TrackExecutionInfo(int trackId, boolean finished, List<ExecutionLogging.LogEntry> logs, int currentTaskIndex) {
        this.trackId = trackId;
        this.finished = finished;
        this.logs = logs;
        this.currentTaskIndex = currentTaskIndex;
    }

    public int getTrackId() {
        return trackId;
    }

    public List<ExecutionLogging.LogEntry> getLogs() {
        return logs;
    }

    public boolean isFinished() {
        return finished;
    }

    public int getCurrentTaskIndex() {
        return currentTaskIndex;
    }
}
