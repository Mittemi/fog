package at.sintrum.fog.simulation.taskengine.tasks;

import at.sintrum.fog.applicationhousing.api.AppEvolutionApi;
import at.sintrum.fog.applicationhousing.api.dto.AppIdentification;
import at.sintrum.fog.simulation.taskengine.TaskListBuilder;

/**
 * Created by Michael Mittermayr on 02.09.2017.
 */
public class RemoveUpgradeInfoTask extends FogTaskBase {

    private final AppEvolutionApi appEvolutionApi;
    private final AppIdentification oldVersion;

    public RemoveUpgradeInfoTask(int offset, TaskListBuilder.TaskListBuilderState.AppTaskBuilder.TrackExecutionState trackExecutionState, AppEvolutionApi appEvolutionApi, AppIdentification oldVersion) {
        super(offset, trackExecutionState, RemoveUpgradeInfoTask.class);
        this.appEvolutionApi = appEvolutionApi;
        this.oldVersion = oldVersion;
    }

    @Override
    protected boolean internalExecute() {
        appEvolutionApi.removeUpdate(oldVersion);
        return true;
    }
}
