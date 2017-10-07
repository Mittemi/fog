package at.sintrum.fog.simulation.service;

import at.sintrum.fog.application.client.factory.ApplicationClientFactory;
import at.sintrum.fog.application.client.factory.TestApplicationClientFactory;
import at.sintrum.fog.deploymentmanager.client.factory.DeploymentManagerClientFactory;
import at.sintrum.fog.metadatamanager.api.ApplicationStateMetadataApi;
import at.sintrum.fog.servercore.service.RequestInfoService;
import at.sintrum.fog.simulation.model.SimulationState;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by Michael Mittermayr on 08.08.2017.
 */
@Service
public class SimulationServiceImpl implements SimulationService {

    private final DeploymentManagerClientFactory deploymentManagerClientFactory;
    private final RedissonClient redissonClient;

    private static final Logger LOG = LoggerFactory.getLogger(SimulationServiceImpl.class);
    private final RequestInfoService requestInfoService;
    private final ApplicationClientFactory applicationClientFactory;

    private final TestApplicationClientFactory testApplicationClientFactory;
    private final ApplicationStateMetadataApi applicationStateMetadataClient;
    private final ScenarioService scenarioService;

    public SimulationServiceImpl(DeploymentManagerClientFactory deploymentManagerClientFactory,
                                 RedissonClient redissonClient,
                                 TestApplicationClientFactory testApplicationClientFactory,
                                 ApplicationStateMetadataApi applicationStateMetadataClient,
                                 RequestInfoService requestInfoService,
                                 ApplicationClientFactory applicationClientFactory,
                                 ScenarioService scenarioService) {
        this.deploymentManagerClientFactory = deploymentManagerClientFactory;
        this.redissonClient = redissonClient;
        this.testApplicationClientFactory = testApplicationClientFactory;
        this.applicationStateMetadataClient = applicationStateMetadataClient;
        this.requestInfoService = requestInfoService;

        this.applicationClientFactory = applicationClientFactory;
        this.scenarioService = scenarioService;
    }


    @Override
    public void processOperation(String instanceId, SimulationState state) {

    }

    @Override
    public void heartbeat(String instanceId) {

    }
}
