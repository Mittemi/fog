package at.sintrum.fog.simulation.taskengine.tasks;

import at.sintrum.fog.applicationhousing.api.AppEvolutionApi;
import at.sintrum.fog.simulation.taskengine.TrackExecutionState;
import org.springframework.util.StringUtils;

/**
 * Created by Michael Mittermayr on 05.09.2017.
 */
public class UpdateInstanceIdTask extends FogTaskBase {

    private final AppEvolutionApi appEvolutionApi;

    public UpdateInstanceIdTask(int offset, TrackExecutionState trackExecutionState, AppEvolutionApi appEvolutionApi) {
        super(offset, trackExecutionState, UpdateInstanceIdTask.class);
        this.appEvolutionApi = appEvolutionApi;
    }

    @Override
    protected boolean internalExecute() {
        String latestInstanceId = appEvolutionApi.getLatestInstanceId(getTrackExecutionState().getInstanceId());
        if (!StringUtils.isEmpty(latestInstanceId) && !getTrackExecutionState().getInstanceId().equals(latestInstanceId)) {
            getTrackExecutionState().setInstanceId(latestInstanceId);
            return true;
        }
        return false;
    }
}
