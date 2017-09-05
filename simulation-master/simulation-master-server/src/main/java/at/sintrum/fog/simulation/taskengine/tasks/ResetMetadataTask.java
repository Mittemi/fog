package at.sintrum.fog.simulation.taskengine.tasks;

import at.sintrum.fog.applicationhousing.api.AppEvolutionApi;
import at.sintrum.fog.applicationhousing.api.AppRecoveryApi;
import at.sintrum.fog.metadatamanager.api.ApplicationStateMetadataApi;
import at.sintrum.fog.simulation.service.FogCellStateService;
import at.sintrum.fog.simulation.service.FogResourceService;
import at.sintrum.fog.simulation.taskengine.TaskListBuilder;

/**
 * Created by Michael Mittermayr on 03.09.2017.
 */
public class ResetMetadataTask extends FogTaskBase {

    private final ApplicationStateMetadataApi applicationStateMetadataApi;
    private final AppEvolutionApi appEvolutionApi;
    private final FogResourceService fogResourceService;
    private final FogCellStateService fogCellStateService;
    private final AppRecoveryApi appRecovery;

    public ResetMetadataTask(int offset, TaskListBuilder.TaskListBuilderState.AppTaskBuilder.TrackExecutionState trackExecutionState, ApplicationStateMetadataApi applicationStateMetadataApi, AppEvolutionApi appEvolutionApi, AppRecoveryApi appRecovery, FogResourceService fogResourceService, FogCellStateService fogCellStateService) {
        super(offset, trackExecutionState, ResetMetadataTask.class);
        this.applicationStateMetadataApi = applicationStateMetadataApi;
        this.appEvolutionApi = appEvolutionApi;
        this.appRecovery = appRecovery;
        this.fogResourceService = fogResourceService;
        this.fogCellStateService = fogCellStateService;
    }

    @Override
    protected boolean internalExecute() {
        reset(applicationStateMetadataApi, appEvolutionApi, appRecovery, fogResourceService, fogCellStateService);
        return true;
    }

    public static void reset(ApplicationStateMetadataApi applicationStateMetadataApi, AppEvolutionApi appEvolutionApi, AppRecoveryApi appRecovery, FogResourceService fogResourceService, FogCellStateService fogCellStateService) {
        fogResourceService.reset();
        appEvolutionApi.reset();
        applicationStateMetadataApi.reset();
        fogCellStateService.reset();
    }
}
