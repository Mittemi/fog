package at.sintrum.fog.simulation.taskengine.tasks;

import at.sintrum.fog.applicationhousing.client.api.AppEvolutionClient;
import at.sintrum.fog.simulation.taskengine.TrackExecutionState;

/**
 * Created by Michael Mittermayr on 05.09.2017.
 */
public class UpdateInstanceIdTask extends FogTaskBase {

    private final AppEvolutionClient appEvolutionClient;

    public UpdateInstanceIdTask(int offset, TrackExecutionState trackExecutionState, AppEvolutionClient appEvolutionClient) {
        super(offset, trackExecutionState, UpdateInstanceIdTask.class);
        this.appEvolutionClient = appEvolutionClient;
    }

    @Override
    protected boolean internalExecute() {
        return updateInstanceId(appEvolutionClient, getTrackExecutionState());
    }
}
