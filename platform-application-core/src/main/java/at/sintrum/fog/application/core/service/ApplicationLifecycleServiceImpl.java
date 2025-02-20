package at.sintrum.fog.application.core.service;

import at.sintrum.fog.applicationhousing.api.dto.AppIdentification;
import at.sintrum.fog.applicationhousing.api.dto.AppUpdateInfo;
import at.sintrum.fog.applicationhousing.client.api.AppEvolutionClient;
import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.core.service.EnvironmentInfoService;
import at.sintrum.fog.deploymentmanager.api.dto.ApplicationMoveRequest;
import at.sintrum.fog.deploymentmanager.api.dto.ApplicationRemoveRequest;
import at.sintrum.fog.deploymentmanager.api.dto.ApplicationUpgradeRequest;
import at.sintrum.fog.deploymentmanager.api.dto.FogOperationResult;
import at.sintrum.fog.deploymentmanager.client.api.ApplicationManagerClient;
import at.sintrum.fog.metadatamanager.api.ApplicationStateMetadataApi;
import at.sintrum.fog.metadatamanager.api.dto.AppState;
import at.sintrum.fog.metadatamanager.api.dto.ApplicationStateMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by Michael Mittermayr on 24.05.2017.
 */
@Service
public class ApplicationLifecycleServiceImpl implements ApplicationLifecycleService, ApplicationListener<ApplicationReadyEvent> {

    // public calls to this service have to be synchronized
    // this allows calls from e.g. the standby service in a scheduled way without concurrency problems

    private final EnvironmentInfoService environmentInfoService;
    private final ApplicationManagerClient applicationManagerClient;
    private final TravelingCoordinationService travelingCoordinationService;
    private final CloudLocatorService cloudLocatorService;
    private final AppEvolutionClient appEvolutionClient;
    private boolean acceptRequests = false;
    private volatile boolean workFinished = false;

    private final ApplicationStateMetadataApi applicationStateMetadataClient;
    private final SimulationClientService simulationClientService;

    private final Logger LOG = LoggerFactory.getLogger(ApplicationLifecycleServiceImpl.class);

    public ApplicationLifecycleServiceImpl(EnvironmentInfoService environmentInfoService,
                                           ApplicationManagerClient applicationManagerClient,
                                           TravelingCoordinationService travelingCoordinationService,
                                           CloudLocatorService cloudLocatorService,
                                           AppEvolutionClient appEvolutionClient,
                                           ApplicationStateMetadataApi applicationStateMetadataClient,
                                           SimulationClientService simulationClientService) {
        this.environmentInfoService = environmentInfoService;
        this.applicationManagerClient = applicationManagerClient;
        this.travelingCoordinationService = travelingCoordinationService;
        this.cloudLocatorService = cloudLocatorService;
        this.appEvolutionClient = appEvolutionClient;
        this.applicationStateMetadataClient = applicationStateMetadataClient;
        this.simulationClientService = simulationClientService;
    }


    private boolean moveApplication(FogIdentification target) {
        synchronized (this) {
            LOG.debug("Request application move to fog: " + target.toUrl());
            setMovingStateMetadata(target);
            travelingCoordinationService.startMove(target);
            // BEGIN Simulation
            simulationClientService.notifyMoving(target);
            // END Simulation
            FogOperationResult result = applicationManagerClient.moveApplication(new ApplicationMoveRequest(environmentInfoService.getOwnContainerId(), target, environmentInfoService.getOwnUrl()));
            if (!result.isSuccessful()) {
                String message = result.getMessage();
                if (message == null) {
                    message = "";
                }
                LOG.debug("Unable to move application to target: " + target.toUrl() + ", Details: " + message);
                setMovingFailedStateMetadata();
            }
            return result.isSuccessful();
        }
    }

    private void setMovingFailedStateMetadata() {
        ApplicationStateMetadata stateMetadata = applicationStateMetadataClient.getById(environmentInfoService.getInstanceId());
        stateMetadata.setRunningAt(FogIdentification.parseFogBaseUrl(environmentInfoService.getFogBaseUrl()));
        stateMetadata.setNextTarget(null);
        stateMetadata.setState(AppState.Moving);
        applicationStateMetadataClient.store(stateMetadata);
    }

    private void setMovingStateMetadata(FogIdentification target) {
        ApplicationStateMetadata stateMetadata = applicationStateMetadataClient.getById(environmentInfoService.getInstanceId());
        stateMetadata.setRunningAt(FogIdentification.parseFogBaseUrl(environmentInfoService.getFogBaseUrl()));
        stateMetadata.setNextTarget(target);
        stateMetadata.setState(AppState.Moving);
        applicationStateMetadataClient.store(stateMetadata);
    }

    private void setRetiredStateMetadata() {
        ApplicationStateMetadata stateMetadata = applicationStateMetadataClient.getById(environmentInfoService.getInstanceId());
        stateMetadata.setRunningAt(FogIdentification.parseFogBaseUrl(environmentInfoService.getFogBaseUrl()));
        stateMetadata.setNextTarget(null);
        stateMetadata.setState(AppState.Retired);
        applicationStateMetadataClient.store(stateMetadata);
    }

    public boolean moveAppIfRequired() {
        synchronized (this) {
            boolean lAcceptRequests = acceptRequests;

            try {
                if (acceptRequests) {
                    acceptRequests = false;
                } else {
                    LOG.warn("Cancel move request. ");
                    return false;
                }
                // we assume work has been finished and we are ready to move somewhere else
                FogIdentification nextTarget = travelingCoordinationService.getNextTarget();

                if (nextTarget != null) {
                    LOG.debug("New application target, let's move");
                    moveApplication(nextTarget);
                } else {
                    if (!environmentInfoService.isCloud()) {
                        LOG.info("Let's move to the cloud, there is no target in queue right now");
                        moveToCloud();
                    }
                }
            } catch (Exception ex) {
                LOG.error("Error in moveAppIfRequired", ex);
                LOG.info("We won't accept any requests. The Recovery should take care of this situation");
                acceptRequests = false; // we need the recovery to take care about this situation
            } finally {
                acceptRequests = lAcceptRequests;
            }
        }
        return true;
    }

    private void moveToCloud() {
        String cloudBaseUrl = cloudLocatorService.getCloudBaseUrl();
        if (!StringUtils.isEmpty(cloudBaseUrl)) {
            moveApplication(FogIdentification.parseFogBaseUrl(cloudBaseUrl));
        } else {
            LOG.warn("We can't move! Cloud was not found.");
        }
    }

    public boolean upgradeAppIfRequired() {
        synchronized (this) {

            boolean lAcceptRequests = acceptRequests;

            try {
                AppUpdateInfo appUpdateInfo = appEvolutionClient.checkForUpdate(new AppIdentification(environmentInfoService.getMetadataId()));

                if (appUpdateInfo.isUpdateRequired()) {
                    LOG.debug("Update is required. Request upgrade!");

                    if (acceptRequests) {
                        acceptRequests = false;
                    } else {
                        LOG.warn("We can't upgrade. Something else is currently in operation");
                        return false;
                    }

                    if (!environmentInfoService.isCloud()) {
                        LOG.debug("Upgrade needs to be executed in the cloud.");
                        moveToCloud();
                    } else {

                        ApplicationUpgradeRequest applicationUpgradeRequest = new ApplicationUpgradeRequest();
                        applicationUpgradeRequest.setContainerId(environmentInfoService.getOwnContainerId());
                        applicationUpgradeRequest.setApplicationUrl(environmentInfoService.getOwnUrl());

                        ApplicationStateMetadata stateMetadata = applicationStateMetadataClient.setState(environmentInfoService.getInstanceId(), AppState.Upgrade);

                        FogOperationResult fogOperationResult = applicationManagerClient.upgradeApplication(applicationUpgradeRequest);

                        if (!fogOperationResult.isSuccessful()) {
                            LOG.debug("Update failed, continue with normal execution");
                            updateAppState(getFogIdentification(), null, stateMetadata);        //TODO: test it...
                            return false;
                        }
                    }
                    return true;
                }

            } catch (Exception ex) {
                LOG.error("Check for updates failed", ex);
            } finally {
                acceptRequests = lAcceptRequests;       //restore
            }
            return false;
        }
    }

    @Scheduled(fixedDelay = 15000)
    public void invoker() {
        if (workFinished) {
            executeNextStep();
        }
    }


    @Override
    public boolean executeNextStep() {
        boolean result = false;
        if (!activeInstanceCheck(environmentInfoService.getInstanceId())) {

            return false;
        }
        if (!upgradeAppIfRequired()) {
            // no upgrade --> check if we should move
            result = moveAppIfRequired();
        }
        // BEGIN Simulation
        simulationClientService.sendHeartbeat();
        // END Simulation
        return result;
    }

    @Override
    public boolean shouldAcceptRequests() {
        return acceptRequests;
    }

    @Override
    public boolean tearDown() {
        synchronized (this) {
            if (!acceptRequests) {
                LOG.warn("Teardown currently not possible");
                return false;
            }
            acceptRequests = false;
            // if this fails, the recovery service will restart the app and everything is fine again
            try {
                setRetiredStateMetadata();
                return applicationManagerClient.removeApplication(new ApplicationRemoveRequest(environmentInfoService.getOwnContainerId(), environmentInfoService.getOwnUrl())).isSuccessful();
            } catch (Exception ex) {
                LOG.error("Teardown failed", ex);
                return false;
            } finally {
                acceptRequests = true;
            }
        }
    }

    @Override
    public void workIsFinished() {
        workFinished = true;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        //TODO: pass right fogIdentification to simulation client (currently it is only the DM but we want the app itself sometimes)
        FogIdentification fogIdentification = getFogIdentification();
        String instanceId = environmentInfoService.getInstanceId();

        synchronized (this) {
            performStartupWork(fogIdentification, instanceId);
            workFinished = environmentInfoService.isCloud();
            acceptRequests = true;
        }
    }

    private void performStartupWork(FogIdentification fogIdentification, String instanceId) {
        activeInstanceCheck(instanceId);

        ApplicationStateMetadata stateMetadata = applicationStateMetadataClient.getById(instanceId);

        if (stateMetadata == null) {
            LOG.debug("First app start. Create state metadata for instance: " + instanceId);
            applicationStateMetadataClient.store(new ApplicationStateMetadata(instanceId, environmentInfoService.getPort(), fogIdentification, getAppState()));
            // BEGIN Simulation
            simulationClientService.notifyStarting();
            // END Simulation
        } else {
            switch (stateMetadata.getState()) {
                case Running:
                    // BEGIN Simulation
                    simulationClientService.notifyStarting();
                    // END Simulation
                    break;
                case Standby:
                    // BEGIN Simulation
                    simulationClientService.notifyStandby();
                    // END Simulation
                    break;
                case Upgrade:
                    LOG.debug("App upgrade finished for instanceId: " + instanceId);
                    updateAppState(fogIdentification, null, stateMetadata);
                    // BEGIN Simulation
                    simulationClientService.notifyUpgrade();
                    // END Simulation
                    break;
                case Moving:
                    LOG.debug("App move finished for instanceId: " + instanceId);
                    travelingCoordinationService.finishMove(fogIdentification);
                    updateAppState(fogIdentification, null, stateMetadata);
                    // BEGIN Simulation
                    simulationClientService.notifyMoved();
                    // END Simulation
                    break;
            }
        }
    }

    private boolean activeInstanceCheck(String instanceId) {
        try {
            boolean activeInstance = applicationStateMetadataClient.isActiveInstance(instanceId);

            if (activeInstance) {
                String latestInstanceId = appEvolutionClient.getLatestInstanceId(instanceId);
                if (latestInstanceId != null && !latestInstanceId.equals(instanceId)) {
                    LOG.error("Something is wrong. This instanceId is not active anymore. Data is corrupted!");
                }
            } else {
                LOG.warn("Instance '" + instanceId + "' is not the active instance anymore.");
                return applicationManagerClient.removeApplication(new ApplicationRemoveRequest(environmentInfoService.getOwnContainerId(), environmentInfoService.getOwnUrl())).isSuccessful();
                //TODO: impl deprecated instance recovery
            }
            return activeInstance;
        } catch (Exception ex) {
            LOG.error("Active instance check failed", ex);
            return true;        //assume yes
        }
    }

    private FogIdentification getFogIdentification() {
        return FogIdentification.parseFogBaseUrl(environmentInfoService.getFogBaseUrl());
    }

    private void updateAppState(FogIdentification currentFog, FogIdentification nextTravelFog, ApplicationStateMetadata stateMetadata) {
        stateMetadata.setRunningAt(currentFog);
        stateMetadata.setNextTarget(nextTravelFog);
        stateMetadata.setState(getAppState());
        applicationStateMetadataClient.store(stateMetadata);
    }

    private AppState getAppState() {

        if (environmentInfoService.hasServiceProfile("standby")) {
            return AppState.Standby;
        }

        return AppState.Running;
    }
}
