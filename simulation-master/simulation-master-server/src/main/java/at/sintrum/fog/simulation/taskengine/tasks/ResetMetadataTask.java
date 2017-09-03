package at.sintrum.fog.simulation.taskengine.tasks;

import at.sintrum.fog.applicationhousing.api.AppEvolutionApi;
import at.sintrum.fog.metadatamanager.api.ApplicationStateMetadataApi;
import at.sintrum.fog.simulation.service.FogResourceService;
import at.sintrum.fog.simulation.taskengine.TaskListBuilder;

/**
 * Created by Michael Mittermayr on 03.09.2017.
 */
public class ResetMetadataTask extends FogTaskBase {

    private final ApplicationStateMetadataApi applicationStateMetadataApi;
    private final AppEvolutionApi appEvolutionApi;
    private final FogResourceService fogResourceService;

    public ResetMetadataTask(int offset, TaskListBuilder.TaskListBuilderState.AppTaskBuilder.TrackExecutionState trackExecutionState, ApplicationStateMetadataApi applicationStateMetadataApi, AppEvolutionApi appEvolutionApi, FogResourceService fogResourceService) {
        super(offset, trackExecutionState, ResetMetadataTask.class);
        this.applicationStateMetadataApi = applicationStateMetadataApi;
        this.appEvolutionApi = appEvolutionApi;
        this.fogResourceService = fogResourceService;
    }

    @Override
    protected boolean internalExecute() {
        reset(applicationStateMetadataApi, appEvolutionApi, fogResourceService);
        return true;
    }

    public static void reset(ApplicationStateMetadataApi applicationStateMetadataApi, AppEvolutionApi appEvolutionApi, FogResourceService fogResourceService) {
        fogResourceService.reset();
        appEvolutionApi.reset();
        applicationStateMetadataApi.reset();
    }
}
