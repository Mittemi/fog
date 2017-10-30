package at.sintrum.fog.deploymentmanager.service;

import at.sintrum.fog.applicationhousing.client.api.AppEvolutionClient;
import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.core.service.EnvironmentInfoService;
import at.sintrum.fog.deploymentmanager.api.dto.*;
import at.sintrum.fog.metadatamanager.api.dto.DockerContainerMetadata;
import at.sintrum.fog.metadatamanager.client.api.ContainerMetadataClient;
import at.sintrum.fog.simulation.api.dto.AppEventInfo;
import at.sintrum.fog.simulation.client.api.SimulationClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by Michael Mittermayr on 09.09.2017.
 */
@Service
public class SimulationFeedbackClientImpl implements SimulationFeedbackClient {

    private final SimulationClient simulationClient;
    private final EnvironmentInfoService environmentInfoService;
    private final FogIdentification location;
    private final ContainerMetadataClient containerMetadataClient;
    private final AppEvolutionClient appEvolutionClient;

    public SimulationFeedbackClientImpl(SimulationClient simulationClient,
                                        EnvironmentInfoService environmentInfoService,
                                        ContainerMetadataClient containerMetadataClient,
                                        AppEvolutionClient appEvolutionClient) {
        this.simulationClient = simulationClient;
        this.environmentInfoService = environmentInfoService;

        location = FogIdentification.parseFogBaseUrl(environmentInfoService.getFogBaseUrl());
        this.containerMetadataClient = containerMetadataClient;
        this.appEvolutionClient = appEvolutionClient;
    }


    @Override
    @Async
    public void appStart(ApplicationStartRequest applicationStartRequest, FogOperationResult fogOperationResult) {
        try {
            simulationClient.started(applicationStartRequest.getInstanceId(), new AppEventInfo(applicationStartRequest.getMetadataId(), null, location, applicationStartRequest.getInstanceId(), applicationStartRequest.getInstanceId(), fogOperationResult.isSuccessful()));
        } catch (Exception ex) {

        }
    }

    @Override
    @Async
    public void appMove(ApplicationMoveRequest applicationMoveRequest, FogOperationResult fogOperationResult) {
        try {
            DockerContainerMetadata containerMetadata = containerMetadataClient.getById(environmentInfoService.getFogId(), applicationMoveRequest.getContainerId());
            simulationClient.moved(containerMetadata.getInstanceId(), new AppEventInfo(containerMetadata.getImageMetadataId(), location, applicationMoveRequest.getTargetFog(), containerMetadata.getInstanceId(), null, fogOperationResult.isSuccessful()));
        } catch (Exception ex) {

        }
    }

    @Override
    @Async
    public void appUpgrade(ApplicationUpgradeRequest applicationUpgradeRequest, FogOperationResult fogOperationResult) {
        try {
            DockerContainerMetadata containerMetadata = containerMetadataClient.getById(environmentInfoService.getFogId(), applicationUpgradeRequest.getContainerId());
            String latestInstanceId = appEvolutionClient.getLatestInstanceId(containerMetadata.getInstanceId());

            simulationClient.upgraded(containerMetadata.getInstanceId(), new AppEventInfo(containerMetadata.getImageMetadataId(), location, location, containerMetadata.getInstanceId(), latestInstanceId, fogOperationResult.isSuccessful()));
        } catch (Exception ex) {

        }
    }

    @Override
    @Async
    public void appRecover(ApplicationRecoveryRequest applicationRecoveryRequest, FogOperationResult fogOperationResult) {
        try {

            String imageMetadataId = null;
            if (!StringUtils.isEmpty(fogOperationResult.getContainerId())) {
                imageMetadataId = containerMetadataClient.getById(environmentInfoService.getFogId(), fogOperationResult.getContainerId()).getImageMetadataId();
            }

            String latestInstanceId = appEvolutionClient.getLatestInstanceId(applicationRecoveryRequest.getInstanceId());
            simulationClient.recovered(applicationRecoveryRequest.getInstanceId(), new AppEventInfo(imageMetadataId, location, location, applicationRecoveryRequest.getInstanceId(), latestInstanceId, fogOperationResult.isSuccessful()));
        } catch (Exception ex) {

        }
    }

    @Override
    @Async
    public void appRemove(ApplicationRemoveRequest applicationRemoveRequest, FogOperationResult fogOperationResult) {
        try {
            DockerContainerMetadata containerMetadata = containerMetadataClient.getById(environmentInfoService.getFogId(), applicationRemoveRequest.getContainerId());
            simulationClient.teardown(containerMetadata.getInstanceId(), new AppEventInfo(containerMetadata.getImageMetadataId(), location, null, containerMetadata.getInstanceId(), null, fogOperationResult.isSuccessful()));
        } catch (Exception ex) {

        }
    }
}
