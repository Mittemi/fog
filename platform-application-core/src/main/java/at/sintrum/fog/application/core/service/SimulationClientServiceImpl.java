package at.sintrum.fog.application.core.service;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.core.service.EnvironmentInfoService;
import at.sintrum.fog.simulation.api.SimulationApi;
import at.sintrum.fog.simulation.api.dto.AppEventInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Created by Michael Mittermayr on 09.08.2017.
 */
@ConditionalOnProperty(prefix = "fog.app", name = "enable-simulation-mode", havingValue = "true")
@Service
public class SimulationClientServiceImpl implements SimulationClientService {

    private final SimulationApi simulationApiClient;
    private final String instanceId;
    private final FogIdentification currentFog;
    private final FogIdentification appIdentification;
    private final CloudLocatorService cloudLocatorService;

    private static final Logger LOG = LoggerFactory.getLogger(SimulationClientServiceImpl.class);

    public SimulationClientServiceImpl(SimulationApi simulationApiClient, EnvironmentInfoService environmentInfoService, CloudLocatorService cloudLocatorService) {
        LOG.debug("Simulation mode enabled");
        this.simulationApiClient = simulationApiClient;
        instanceId = environmentInfoService.getInstanceId();
        currentFog = FogIdentification.parseFogBaseUrl(environmentInfoService.getFogBaseUrl());
        appIdentification = new FogIdentification(environmentInfoService.getEurekaClientIp(), environmentInfoService.getPort());
        this.cloudLocatorService = cloudLocatorService;
    }


    @Override
    @Async
    public void sendHeartbeat() {
        LOG.debug("Send heartbeat");
        //simulationApiClient.sendHeartbeat(instanceId);
    }

    @Override
    @Async
    public void notifyStarting() {
        LOG.debug("Notify starting");
        simulationApiClient.starting(instanceId, new AppEventInfo(currentFog, currentFog, instanceId, instanceId, true));
    }

    @Override
    @Async
    public void notifyMoving(FogIdentification target) {
        LOG.debug("Notify moving");
        simulationApiClient.moving(instanceId, new AppEventInfo(currentFog, target, instanceId, instanceId, true));
    }

    @Override
    @Async
    public void notifyMoved() {
        LOG.debug("Notify moved");
        simulationApiClient.moved(instanceId, new AppEventInfo(currentFog, currentFog, instanceId, instanceId, true));
    }

    @Override
    @Async
    public void notifyStandby() {
        LOG.debug("Notify standby");
        simulationApiClient.standby(instanceId, new AppEventInfo(currentFog, currentFog, instanceId, instanceId, true));
    }

    @Override
    @Async
    public void notifyUpgrade() {
        LOG.debug("Notify upgrade");
        FogIdentification cloud = FogIdentification.parseFogBaseUrl(cloudLocatorService.getCloudBaseUrl());
        simulationApiClient.upgrading(instanceId, new AppEventInfo(currentFog, cloud, instanceId, null, true));
    }
}
