package at.sintrum.fog.applicationhousing.recovery;

import at.sintrum.fog.application.client.ApplicationClientFactory;
import at.sintrum.fog.application.core.api.ApplicationInfoApi;
import at.sintrum.fog.application.core.api.dto.AppInfo;
import at.sintrum.fog.applicationhousing.recovery.metadata.AppRuntimeMetadata;
import at.sintrum.fog.applicationhousing.recovery.metadata.FogCellMetadata;
import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.deploymentmanager.api.dto.ApplicationRecoveryRequest;
import at.sintrum.fog.deploymentmanager.api.dto.FogOperationResult;
import at.sintrum.fog.deploymentmanager.client.api.ApplicationManager;
import at.sintrum.fog.deploymentmanager.client.factory.DeploymentManagerClientFactory;
import at.sintrum.fog.metadatamanager.api.ApplicationStateMetadataApi;
import at.sintrum.fog.metadatamanager.api.dto.AppState;
import at.sintrum.fog.metadatamanager.api.dto.ApplicationStateMetadata;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Michael Mittermayr on 21.07.2017.
 */
@Service
public class ApplicationRecoveryImpl {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationRecoveryImpl.class);

    private boolean isCheckRunning = false;
    private DiscoveryClient discoveryClient;
    private final ApplicationStateMetadataApi applicationStateMetadataApi;
    private final ApplicationClientFactory applicationClientFactory;
    private final DeploymentManagerClientFactory deploymentManagerClientFactory;
    private FogIdentification cloud;

    // key: instanceId
    private final Map<String, AppRuntimeMetadata> appRuntimeMetadata;

    // key: url
    private final Map<String, FogCellMetadata> fogCellMetadata;

    public ApplicationRecoveryImpl(DiscoveryClient discoveryClient,
                                   ApplicationStateMetadataApi applicationStateMetadataApi,
                                   ApplicationClientFactory applicationClientFactory,
                                   DeploymentManagerClientFactory deploymentManagerClientFactory) {
        this.discoveryClient = discoveryClient;
        this.applicationStateMetadataApi = applicationStateMetadataApi;
        this.applicationClientFactory = applicationClientFactory;
        this.deploymentManagerClientFactory = deploymentManagerClientFactory;
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

        updateKnownApps();      //TODO: multithreaded

        recoverApps();          //TODO: multithreaded
    }

    private void recoverApps() {

        for (String instanceId : appRuntimeMetadata.keySet()) {
            AppRuntimeMetadata appRuntimeMetadata = this.appRuntimeMetadata.get(instanceId);
            if (appRuntimeMetadata.isIgnored() || appRuntimeMetadata.isRetired()) {
                continue;
            }

            if (appRuntimeMetadata.hasTimeout()) {
                LOG.warn("AppTimeout: " + appRuntimeMetadata.getInstanceId());

                if (!applicationStateMetadataApi.isActiveInstance(appRuntimeMetadata.getInstanceId())) {
                    LOG.debug("This is not an active instance, no need for recovery: " + appRuntimeMetadata.getInstanceId());
                    appRuntimeMetadata.setRetired(true);
                    continue;
                }

                //1. check if app should be running
                //2. check if app is reachable
                //3. check if fog is reachable
                //4a. tell deployment manager to recover app
                //4b. recover app in cloud
                ApplicationStateMetadata stateMetadata = applicationStateMetadataApi.getById(appRuntimeMetadata.getInstanceId());
                if (shouldRun(stateMetadata, appRuntimeMetadata)) {
                    FogIdentification applicationUrl = applicationStateMetadataApi.getApplicationUrl(appRuntimeMetadata.getInstanceId());
//                    boolean isAppReachable = isAppReachable(applicationUrl);

                    if (appRuntimeMetadata.getLastRecoveryCall() == null || Seconds.secondsBetween(appRuntimeMetadata.getLastRecoveryCall(), new DateTime()).isGreaterThan(Seconds.seconds(60))) {
                        LOG.debug("Recover app: " + appRuntimeMetadata.getInstanceId());

                        if (tryRecoverApp(appRuntimeMetadata, stateMetadata)) {

                            LOG.debug("Failed to recovery in fog cell, start recovery in cloud: " + appRuntimeMetadata.getInstanceId());
                            if (!applicationStateMetadataApi.deprecateInstance(appRuntimeMetadata.getInstanceId())) {
                                LOG.warn("Failed to deprecate instance: " + appRuntimeMetadata.getInstanceId());
                            }

                            //TODO: recover app in cloud
                        }

                    } else {
                        LOG.debug("Recovery has been called recent for this application. Skip recovery for: " + appRuntimeMetadata.getInstanceId());
                    }
                }
            }
        }
    }

    private boolean tryRecoverApp(AppRuntimeMetadata appRuntimeMetadata, ApplicationStateMetadata stateMetadata) {
        try {
            appRuntimeMetadata.setLastRecoveryCall(new DateTime());
            ApplicationManager applicationManagerClient = deploymentManagerClientFactory.createApplicationManagerClient(stateMetadata.getRunningAt().toUrl());
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

    private boolean isAppReachable(FogIdentification applicationUrl) {
        ApplicationInfoApi applicationInfoClient = applicationClientFactory.createApplicationInfoClient(applicationUrl.toUrl());

        try {
            AppInfo info = applicationInfoClient.info();
            return info != null;
        } catch (Exception ex) {
            LOG.error("Failed to get appinfo", ex);
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
        for (AppRuntimeMetadata runtimeMetadata : appRuntimeMetadata.values()) {

            if (runtimeMetadata.isRetired() || runtimeMetadata.isIgnored()) continue;

            FogIdentification applicationUrl = applicationStateMetadataApi.getApplicationUrl(runtimeMetadata.getInstanceId());
            if (isAppReachable(applicationUrl)) {
                LOG.debug("App " + runtimeMetadata.getInstanceId() + " is healthy");
                runtimeMetadata.heartbeat();
            } else {
                LOG.warn("App: " + runtimeMetadata.getServiceId() + " (" + runtimeMetadata.getInstanceId() + ") is not reachable");
            }
        }
    }

    private void updateAppMetadata(String serviceId) {

        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);

        for (ServiceInstance instance : instances) {
            Map<String, String> metadata = instance.getMetadata();

            String instanceID = metadata.get("fogInstanceId");
            if (!StringUtils.isEmpty(instanceID)) {
                appRuntimeMetadata.putIfAbsent(instanceID, new AppRuntimeMetadata(serviceId, instanceID));
            }
        }
    }

    private void updateKnownFogs() {

        for (FogCellMetadata item : getDeploymentManagers()) {
            fogCellMetadata.putIfAbsent(item.getFogIdentification().toUrl(), item);
            fogCellMetadata.get(item.getFogIdentification().toUrl()).heartbeat();
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
