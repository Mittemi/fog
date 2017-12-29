package at.sintrum.fog.simulation.taskengine.tasks;

import at.sintrum.fog.applicationhousing.api.AppEvolutionApi;
import at.sintrum.fog.applicationhousing.api.AppRecoveryApi;
import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.deploymentmanager.client.factory.DeploymentManagerClientFactory;
import at.sintrum.fog.metadatamanager.api.ApplicationStateMetadataApi;
import at.sintrum.fog.metadatamanager.client.api.AppRequestClient;
import at.sintrum.fog.simulation.service.FogCellStateService;
import at.sintrum.fog.simulation.service.FogResourceService;
import at.sintrum.fog.simulation.taskengine.TrackExecutionState;

import java.util.List;

/**
 * Created by Michael Mittermayr on 03.09.2017.
 */
public class ResetMetadataTask extends FogTaskBase {

    private final List<FogIdentification> fogs;
    private final DeploymentManagerClientFactory deploymentManagerClientFactory;
    private final ApplicationStateMetadataApi applicationStateMetadataApi;
    private final AppEvolutionApi appEvolutionApi;
    private final FogResourceService fogResourceService;
    private final FogCellStateService fogCellStateService;
    private final AppRecoveryApi appRecovery;
    private final AppRequestClient appRequestClient;

    public ResetMetadataTask(int offset, TrackExecutionState trackExecutionState, List<FogIdentification> fogs, DeploymentManagerClientFactory deploymentManagerClientFactory, ApplicationStateMetadataApi applicationStateMetadataApi, AppEvolutionApi appEvolutionApi, AppRecoveryApi appRecovery, FogResourceService fogResourceService, FogCellStateService fogCellStateService, AppRequestClient appRequestClient) {
        super(offset, trackExecutionState, ResetMetadataTask.class);
        this.fogs = fogs;
        this.deploymentManagerClientFactory = deploymentManagerClientFactory;
        this.applicationStateMetadataApi = applicationStateMetadataApi;
        this.appEvolutionApi = appEvolutionApi;
        this.appRecovery = appRecovery;
        this.fogResourceService = fogResourceService;
        this.fogCellStateService = fogCellStateService;
        this.appRequestClient = appRequestClient;
    }

    @Override
    protected boolean internalExecute() {
        reset(fogs, deploymentManagerClientFactory, applicationStateMetadataApi, appEvolutionApi, appRecovery, fogResourceService, fogCellStateService, appRequestClient);
        return true;
    }

    public static void reset(List<FogIdentification> fogs, DeploymentManagerClientFactory deploymentManagerClientFactory, ApplicationStateMetadataApi applicationStateMetadataApi, AppEvolutionApi appEvolutionApi, AppRecoveryApi appRecovery, FogResourceService fogResourceService, FogCellStateService fogCellStateService, AppRequestClient appRequestClient) {

        for (FogIdentification fogIdentification : fogs) {
            deploymentManagerClientFactory.createApplicationManagerClient(fogIdentification.toUrl()).reset();
        }
        fogResourceService.reset();
        appEvolutionApi.reset();
        applicationStateMetadataApi.reset();
        fogCellStateService.reset();
        appRequestClient.reset();
    }
}
