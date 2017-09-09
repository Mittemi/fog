package at.sintrum.fog.simulation.scenario.dto;

import java.util.List;

/**
 * Created by Michael Mittermayr on 02.09.2017.
 */
public class ScenarioExecutionInfo {

    private String name;

    private boolean isFinished;

    private final List<TrackExecutionInfo> trackExecutionInfos;

    public ScenarioExecutionInfo(String name, boolean isFinished, List<TrackExecutionInfo> trackExecutionInfos) {
        this.name = name;
        this.isFinished = isFinished;
        this.trackExecutionInfos = trackExecutionInfos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public List<TrackExecutionInfo> getTrackExecutionInfos() {
        return trackExecutionInfos;
    }
}
