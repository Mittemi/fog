package at.sintrum.fog.simulation.service;

import at.sintrum.fog.application.api.WorkApi;
import at.sintrum.fog.application.client.ApplicationClientFactory;
import at.sintrum.fog.application.client.api.TestApplicationClientFactory;
import at.sintrum.fog.application.core.api.AppLifecycleApi;
import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.deploymentmanager.api.dto.ApplicationStartRequest;
import at.sintrum.fog.deploymentmanager.api.dto.FogOperationResult;
import at.sintrum.fog.deploymentmanager.client.api.ApplicationManager;
import at.sintrum.fog.deploymentmanager.client.factory.DeploymentManagerClientFactory;
import at.sintrum.fog.metadatamanager.api.ApplicationStateMetadataApi;
import at.sintrum.fog.servercore.service.RequestInfoService;
import at.sintrum.fog.simulation.api.dto.SimulationStartInfoDto;
import at.sintrum.fog.simulation.model.ApplicationExecutionStatus;
import at.sintrum.fog.simulation.model.SimulationState;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by Michael Mittermayr on 08.08.2017.
 */
@Service
public class SimulationServiceImpl implements SimulationService {

    private final DeploymentManagerClientFactory deploymentManagerClientFactory;
    private final RedissonClient redissonClient;

    private static final Logger LOG = LoggerFactory.getLogger(SimulationServiceImpl.class);
    private final RMap<String, SimulationStartInfoDto> simulationStartInfos;
    private final RMap<String, ApplicationExecutionStatus> applicationExecutionStatus;
    private final RequestInfoService requestInfoService;
    private final ApplicationClientFactory applicationClientFactory;

    private final TestApplicationClientFactory testApplicationClientFactory;
    private final ApplicationStateMetadataApi applicationStateMetadataClient;

    public SimulationServiceImpl(DeploymentManagerClientFactory deploymentManagerClientFactory,
                                 RedissonClient redissonClient,
                                 TestApplicationClientFactory testApplicationClientFactory,
                                 ApplicationStateMetadataApi applicationStateMetadataClient,
                                 RequestInfoService requestInfoService,
                                 ApplicationClientFactory applicationClientFactory) {
        this.deploymentManagerClientFactory = deploymentManagerClientFactory;
        this.redissonClient = redissonClient;
        this.testApplicationClientFactory = testApplicationClientFactory;
        this.applicationStateMetadataClient = applicationStateMetadataClient;

        // we use redisson to preserve state beyond service restarts during development
        simulationStartInfos = redissonClient.getMap("Simulation.StartInfo.Map");
        applicationExecutionStatus = redissonClient.getMap("Simulation.ApplicationExecutionStatus.Map");
        this.requestInfoService = requestInfoService;

        this.applicationClientFactory = applicationClientFactory;
    }


    @Override
    public void startSimulation(SimulationStartInfoDto startInfoDto) {
        ApplicationManager applicationManagerClient = deploymentManagerClientFactory.createApplicationManagerClient(startInfoDto.getCloud().toUrl());

        String instanceId = UUID.randomUUID().toString();
        simulationStartInfos.put(instanceId, startInfoDto);
        FogOperationResult fogOperationResult = applicationManagerClient.requestApplicationStart(new ApplicationStartRequest(startInfoDto.getMetadataId(), instanceId));

        if (fogOperationResult.isSuccessful()) {
            ApplicationExecutionStatus value = new ApplicationExecutionStatus();
            value.setMetadataId(startInfoDto.getMetadataId());
            value.setCurrentEnvironment(startInfoDto.getCloud());
            applicationExecutionStatus.put(instanceId, value);
        }
    }

    @Override
    public void processOperation(String instanceId, SimulationState state) {

        SimulationStartInfoDto simulationStartInfoDto = simulationStartInfos.get(instanceId);

        //try (MDC.putCloseable("InstanceId", instanceId))
        //TODO: logging
        {

            switch (state) {
                case Starting:
                    // create initial request
                    queueAppRequest(instanceId, simulationStartInfoDto, 0);
                    break;
                case Working:
                    WorkApi workClient = testApplicationClientFactory.createWorkClient(applicationStateMetadataClient.getApplicationUrl(instanceId).toUrl());
                    try {
                        String s = workClient.doSomeWork();
                    } catch (Exception ex) {
                        LOG.debug("WorkerApiClient failed", ex);
                    }
                    LOG.info("Finished work: " + instanceId);
                    break;

                case Moving:
                    break;
                case Upgrading:
                    break;
                case Standby:
                    // back to standby --> create new requests
                    for (int i = 0; i < simulationStartInfoDto.getFogs().length; i++) {
                        queueAppRequest(instanceId, simulationStartInfoDto, i);
                    }
                    break;
            }
        }
    }

    @Override
    public void heartbeat(String instanceId) {
        LOG.debug("Heartbeat from: " + instanceId);
    }

    private void queueAppRequest(String instanceId, SimulationStartInfoDto simulationStartInfoDto, int iteration) {
        FogIdentification[] fogs = simulationStartInfoDto.getFogs();

        FogIdentification applicationUrl = applicationStateMetadataClient.getApplicationUrl(instanceId);
        AppLifecycleApi platformAppClient = applicationClientFactory.createAppLifecycleClient(applicationUrl.toUrl());

        FogIdentification fog = fogs[iteration % fogs.length];
        boolean b = platformAppClient.requestApplication(fog);
    }

    public void continueSimulation(String instanceId) {

        SimulationStartInfoDto startInfo = simulationStartInfos.get(instanceId);
        ApplicationExecutionStatus execStatus = applicationExecutionStatus.get(instanceId);


    }
}
