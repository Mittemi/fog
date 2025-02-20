package at.sintrum.fog.applicationhousing.recovery;

import at.sintrum.fog.application.client.api.ApplicationInfoClient;
import at.sintrum.fog.application.client.factory.ApplicationClientFactory;
import at.sintrum.fog.applicationhousing.api.dto.AppInstanceIdHistoryInfo;
import at.sintrum.fog.applicationhousing.config.AppHousingConfigurationProperties;
import at.sintrum.fog.applicationhousing.recovery.metadata.AppRuntimeMetadata;
import at.sintrum.fog.applicationhousing.recovery.metadata.FogCellMetadata;
import at.sintrum.fog.applicationhousing.service.InstanceIdHistoryService;
import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.deploymentmanager.api.dto.ApplicationRecoveryRequest;
import at.sintrum.fog.deploymentmanager.api.dto.ApplicationStartRequest;
import at.sintrum.fog.deploymentmanager.api.dto.FogOperationResult;
import at.sintrum.fog.deploymentmanager.client.api.ApplicationManagerClient;
import at.sintrum.fog.deploymentmanager.client.factory.DeploymentManagerClientFactory;
import at.sintrum.fog.metadatamanager.api.ApplicationStateMetadataApi;
import at.sintrum.fog.metadatamanager.api.ContainerMetadataApi;
import at.sintrum.fog.metadatamanager.api.dto.AppState;
import at.sintrum.fog.metadatamanager.api.dto.ApplicationStateMetadata;
import at.sintrum.fog.metadatamanager.api.dto.DockerContainerMetadata;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Michael Mittermayr on 21.07.2017.
 */
@Service
@ConditionalOnProperty(name = "fog.apphousing.enableRecovery", havingValue = "true")
public class ApplicationRecoveryImpl implements ApplicationRecovery {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationRecoveryImpl.class);

    private boolean isCheckRunning = false;
    private DiscoveryClient discoveryClient;
    private final ApplicationStateMetadataApi applicationStateMetadataApi;
    private final ApplicationClientFactory applicationClientFactory;
    private final DeploymentManagerClientFactory deploymentManagerClientFactory;
    private final ContainerMetadataApi containerMetadataApi;
    private final InstanceIdHistoryService instanceIdHistoryService;
    private final AppHousingConfigurationProperties configurationProperties;
    private FogIdentification cloud;

    // key: instanceId
    private final Map<String, AppRuntimeMetadata> appRuntimeMetadata;

    // key: url
    private final Map<String, FogCellMetadata> fogCellMetadata;

    public ApplicationRecoveryImpl(DiscoveryClient discoveryClient,
                                   ApplicationStateMetadataApi applicationStateMetadataApi,
                                   ApplicationClientFactory applicationClientFactory,
                                   DeploymentManagerClientFactory deploymentManagerClientFactory,
                                   ContainerMetadataApi containerMetadataApi,
                                   InstanceIdHistoryService instanceIdHistoryService, AppHousingConfigurationProperties configurationProperties) {
        this.discoveryClient = discoveryClient;
        this.applicationStateMetadataApi = applicationStateMetadataApi;
        this.applicationClientFactory = applicationClientFactory;
        this.deploymentManagerClientFactory = deploymentManagerClientFactory;
        this.containerMetadataApi = containerMetadataApi;
        this.instanceIdHistoryService = instanceIdHistoryService;
        this.configurationProperties = configurationProperties;
        appRuntimeMetadata = new HashMap<>();
        fogCellMetadata = new HashMap<>();
    }

    @Scheduled(fixedDelay = 15000)
    public synchronized void runChecks() {
        LOG.debug("Run app recovery checks");
        if (isCheckRunning) {
            LOG.debug("Skip this run, previous one has not completed yet!");
            return;
        }
        isCheckRunning = true;
        try {
            checkApplications();
        } catch (Exception ex) {
            LOG.error("Error during recovery checks", ex);
        } finally {
            isCheckRunning = false;
        }
    }

    private synchronized void checkApplications() {
        updateKnownFogs();

        updateKnownApps();  //async!

        recoverApps();      //async!
    }

    private void recoverApps() {

        new LinkedList<>(appRuntimeMetadata.keySet())
                .parallelStream()
                .forEach(this::recoverAppInstance);
    }

    private void recoverAppInstance(String instanceId) {
        AppRuntimeMetadata appRuntimeMetadata = this.appRuntimeMetadata.get(instanceId);
        if (appRuntimeMetadata.isIgnored() || appRuntimeMetadata.isRetired()) {
            return;
        }

        if (appRuntimeMetadata.hasTimeout(configurationProperties.getAppHeartbeatTimeout())) {
            LOG.warn("AppTimeout: " + appRuntimeMetadata.getInstanceId());

            if (!applicationStateMetadataApi.isActiveInstance(appRuntimeMetadata.getInstanceId())) {
                LOG.debug("This is not an active instance, no need for recovery: " + appRuntimeMetadata.getInstanceId());
                appRuntimeMetadata.setRetired(true);
                return;
            }

            //1. check if app should be running
            //2. check if app is reachable
            //3. check if fog is reachable
            //4a. tell deployment manager to recover app
            //4b. recover app in cloud
            ApplicationStateMetadata stateMetadata = applicationStateMetadataApi.getById(appRuntimeMetadata.getInstanceId());
            if (stateMetadata == null) {
                LOG.warn("No state metadata. This might be due to a reset call");
                if (appRuntimeMetadata.hasTimeout(configurationProperties.getObsoleteMetadataTimeout())) {
                    LOG.debug("Remove obsolete metadata for: " + appRuntimeMetadata.getInstanceId());
                    this.appRuntimeMetadata.remove(appRuntimeMetadata.getInstanceId());
                }
                return;
            }

            if (!hasTimeout(stateMetadata.getLastUpdate(), configurationProperties.getAppStateMetadataGraceTimeout())) {
                LOG.debug("State metadata update within grace period. Skip this recovery call for instance: " + appRuntimeMetadata.getInstanceId());
                return;
            }

            if (shouldRun(stateMetadata, appRuntimeMetadata)) {
                FogIdentification applicationUrl = applicationStateMetadataApi.getApplicationUrl(appRuntimeMetadata.getInstanceId());
                if (isAppReachable(applicationUrl)) {
                    LOG.debug("App is alive, nothing to recover");
                    return;
                }

                if (appRuntimeMetadata.getLastRecoveryCall() == null || hasTimeout(appRuntimeMetadata.getLastRecoveryCall(), configurationProperties.getAppRecoveryWaitTime())) {
                    LOG.debug("Recover app: " + appRuntimeMetadata.getInstanceId());

                    if (!tryRecoverApp(appRuntimeMetadata, stateMetadata)) {
                        recoverAppInCloud(appRuntimeMetadata, stateMetadata);
                    }
                } else {
                    LOG.debug("Recovery has been called recent for this application. Skip recovery for: " + appRuntimeMetadata.getInstanceId());
                }
            }
        }
    }

    private boolean hasTimeout(DateTime dateTime, int seconds) {
        return Seconds.secondsBetween(dateTime, new DateTime()).isGreaterThan(Seconds.seconds(seconds));
    }

    private boolean recoverAppInCloud(AppRuntimeMetadata appRuntimeMetadata, ApplicationStateMetadata stateMetadata) {

        FogCellMetadata cloud = getCloud();
        if (cloud == null) {
            LOG.error("Failed to identify the cloud. Unable to run recovery.");
            return false;
        } else {
            DockerContainerMetadata containerMetadata = containerMetadataApi.getLatestByInstanceId(appRuntimeMetadata.getInstanceId());

            if (containerMetadata == null) {
                LOG.error("Failed to recover app, container metadata not found for: " + appRuntimeMetadata.getInstanceId());
                return false;
            }

            LOG.debug("Failed to recovery in fog cell, start recovery in cloud: " + appRuntimeMetadata.getInstanceId());
            ApplicationManagerClient applicationManagerClient = deploymentManagerClientFactory.createApplicationManagerClient(cloud.getFogIdentification().toUrl());
            appRuntimeMetadata.setLastRecoveryCall(new DateTime());

            if (!applicationStateMetadataApi.deprecateInstance(appRuntimeMetadata.getInstanceId())) {
                LOG.warn("Failed to deprecate instance: " + appRuntimeMetadata.getInstanceId());
            }

            FogOperationResult fogOperationResult = applicationManagerClient.requestApplicationStart(new ApplicationStartRequest(containerMetadata.getImageMetadataId(), null));
            if (fogOperationResult.isSuccessful()) {
                LOG.debug("Recovered app: " + appRuntimeMetadata.getInstanceId() + " in cloud. New InstanceID: " + fogOperationResult.getInstanceId());

                if (!instanceIdHistoryService.addToInstanceIdHistory(new AppInstanceIdHistoryInfo(appRuntimeMetadata.getInstanceId(), fogOperationResult.getInstanceId()))) {
                    LOG.warn("Failed to update instanceId history");
                }
                return true;
            } else {
                LOG.debug("Failed to recover app: " + appRuntimeMetadata.getInstanceId());
                LOG.error("This is a major problem. We should not be in this state. There is no way implemented to fix this situation.");
                //TODO: app should not be deprecated otherwise we have no app running anymore
                return false;
            }
        }
    }

    private boolean tryRecoverApp(AppRuntimeMetadata appRuntimeMetadata, ApplicationStateMetadata stateMetadata) {
        try {
            appRuntimeMetadata.setLastRecoveryCall(new DateTime());
            ApplicationManagerClient applicationManagerClient = deploymentManagerClientFactory.createApplicationManagerClient(stateMetadata.getRunningAt().toUrl());
            FogOperationResult fogOperationResult = applicationManagerClient.recoverApplication(new ApplicationRecoveryRequest(stateMetadata.getInstanceId()));

            if (!fogOperationResult.isSuccessful()) {
                LOG.error("Recovery for app: " + appRuntimeMetadata.getInstanceId() + " failed with message: " + fogOperationResult.getMessage());
                return false;
            }
            return true;
        } catch (Exception ex) {
            LOG.warn("Failed to request recovery for: " + appRuntimeMetadata.getInstanceId() + " at " + stateMetadata.getRunningAt().toUrl());
            return false;
        }
    }

    @Override
    public void reset() {
        appRuntimeMetadata.clear();
        fogCellMetadata.clear();
    }

    private boolean isFogReachable(FogIdentification fogIdentification) {
        if (fogIdentification == null) return false;

        ApplicationManagerClient applicationManagerClient = deploymentManagerClientFactory.createApplicationManagerClient(fogIdentification.toUrl());

        try {
            return applicationManagerClient.isAlive();
        } catch (Exception ex) {
            LOG.trace("Fog not reachable: " + fogIdentification.toFogId());
        }
        return false;
    }

    private boolean isAppReachable(FogIdentification applicationUrl) {
        if (applicationUrl == null) return false;

        ApplicationInfoClient applicationInfoClient = applicationClientFactory.createApplicationInfoClient(applicationUrl.toUrl());

        try {
            return applicationInfoClient.isAlive();
        } catch (Exception ex) {
            LOG.trace("App not reachable: " + applicationUrl.toFogId());
        }
        return false;
    }

    private boolean shouldRun(ApplicationStateMetadata stateMetadata, AppRuntimeMetadata appRuntimeMetadata) {
        try {

            if (stateMetadata.getState().equals(AppState.Retired)) {
                LOG.debug("App '" + appRuntimeMetadata.getInstanceId() + "' is retired and should not be running anymore");
                appRuntimeMetadata.setRetired(true);
            }

            switch (stateMetadata.getState()) {
                case Standby:
                case Running:
                    return true;
                case Upgrade:
                case Moving:
                    return false;
                case Retired:
                    return false;
            }

        } catch (Exception ex) {
            LOG.error("Failed to get state metadata", ex);
        }
        return true;
    }

    private void updateKnownApps() {
        for (String serviceId : discoveryClient.getServices()) {
            updateAppMetadata(serviceId);
        }

        appRuntimeMetadata.values()
                .parallelStream()
                .filter(runtimeMetadata -> !(runtimeMetadata.isRetired() || runtimeMetadata.isIgnored()))
                .forEach((runtimeMetadata) -> {

                    FogIdentification applicationUrl = applicationStateMetadataApi.getApplicationUrl(runtimeMetadata.getInstanceId());
                    if (isAppReachable(applicationUrl)) {
                        LOG.debug("App " + runtimeMetadata.getInstanceId() + " is healthy");
                        runtimeMetadata.heartbeat();
                    } else {
                        LOG.warn("App: " + runtimeMetadata.getServiceId() + " (" + runtimeMetadata.getInstanceId() + ") is not reachable");
                    }
                });
    }

    private void updateAppMetadata(String serviceId) {

        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);

        for (ServiceInstance instance : instances) {
            Map<String, String> metadata = instance.getMetadata();

            String instanceID = metadata.get("fogInstanceId");
            if (!StringUtils.isEmpty(instanceID) && !appRuntimeMetadata.containsKey(instanceID)) {
                if (applicationStateMetadataApi.isActiveInstance(instanceID)) {
                    LOG.debug("Register new application for recovery: " + instanceID);
                    appRuntimeMetadata.putIfAbsent(instanceID, new AppRuntimeMetadata(serviceId, instanceID));
                }
            }
        }
    }

    private void updateKnownFogs() {

        for (FogCellMetadata item : getDeploymentManagers()) {
            FogIdentification fogIdentification = item.getFogIdentification();
            fogCellMetadata.putIfAbsent(fogIdentification.toUrl(), item);
            if (isFogReachable(fogIdentification)) {
                fogCellMetadata.get(fogIdentification.toUrl()).heartbeat();
            }
        }

    }

    public FogCellMetadata getCloud() {
        return getDeploymentManagers().stream().filter(FogCellMetadata::isCloud).findFirst().orElse(null);
    }

    public List<FogCellMetadata> getDeploymentManagers() {
        List<ServiceInstance> instances = discoveryClient.getInstances("deployment-manager");

        return instances
                .stream()
                .map(x -> new FogCellMetadata("deployment-manager", new FogIdentification(x.getHost(), x.getPort()), x.getMetadata().get("profiles")))
                .collect(Collectors.toList());
    }
}
