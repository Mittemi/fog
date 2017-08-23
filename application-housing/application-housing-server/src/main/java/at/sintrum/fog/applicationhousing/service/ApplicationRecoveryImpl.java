package at.sintrum.fog.applicationhousing.service;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.metadatamanager.api.ApplicationStateMetadataApi;
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

    // key: instanceId
    private final Map<String, AppRuntimeMetadata> appRuntimeMetadata;

    // key: url
    private final Map<String, FogCellMetadata> fogCellMetadata;

    public ApplicationRecoveryImpl(DiscoveryClient discoveryClient, ApplicationStateMetadataApi applicationStateMetadataApi) {
        this.discoveryClient = discoveryClient;
        this.applicationStateMetadataApi = applicationStateMetadataApi;
        appRuntimeMetadata = new HashMap<>();
        fogCellMetadata = new HashMap<>();
    }

    @Scheduled(fixedDelay = 60000)
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

        updateKnownApps();

        recoverApps();
    }

    private void recoverApps() {

        for (String instanceId : appRuntimeMetadata.keySet()) {
            AppRuntimeMetadata appRuntimeMetadata = this.appRuntimeMetadata.get(instanceId);
            if (appRuntimeMetadata.isIgnored()) {
                continue;
            }

            if (appRuntimeMetadata.hasTimeout()) {
                LOG.warn("AppTimeout: " + appRuntimeMetadata.getInstanceId());

                //1. check if app should be running
                //2. check if app is reachable
                //3. check if fog is reachable
                //4a. tell deployment manager to recover app
                //4b. recover app in cloud

            }
        }
    }

    private void updateKnownApps() {
        for (String serviceId : discoveryClient.getServices()) {
            updateAppMetadata(serviceId);
        }
    }

    private void updateAppMetadata(String serviceId) {

        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);

        for (ServiceInstance instance : instances) {
            Map<String, String> metadata = instance.getMetadata();

            String instanceID = metadata.get("fogInstanceId");
            if (!StringUtils.isEmpty(instanceID)) {
                appRuntimeMetadata.putIfAbsent(instanceID, new AppRuntimeMetadata(serviceId, instanceID));
                appRuntimeMetadata.get(instanceID).heartbeat(); //TODO: check this
            }

        }

        if (!appRuntimeMetadata.containsKey(serviceId)) {
            // ignore this service (not an application)
            return;
        }


        //TODO: ask app for health state
    }

    private void updateKnownFogs() {

        for (FogIdentification fogIdentification : getDeploymentManagers()) {
            fogCellMetadata.putIfAbsent(fogIdentification.toUrl(), new FogCellMetadata("deployment-manager", fogIdentification));
            fogCellMetadata.get(fogIdentification.toUrl()).heartbeat();
        }

    }

    public List<FogIdentification> getDeploymentManagers() {
        return discoveryClient.getInstances("deployment-manager").stream().map(x -> new FogIdentification(x.getHost(), x.getPort())).collect(Collectors.toList());
    }


    class RuntimeMetadataBase {
        private final String serviceId;

        private DateTime timeAdded;
        private DateTime lastTimeActive;

        RuntimeMetadataBase(String serviceId) {
            this.serviceId = serviceId;
            timeAdded = new DateTime();
        }

        public DateTime getTimeAdded() {
            return timeAdded;
        }

        public void setTimeAdded(DateTime timeAdded) {
            this.timeAdded = timeAdded;
        }

        public DateTime getLastTimeActive() {
            return lastTimeActive;
        }

        public void setLastTimeActive(DateTime lastTimeActive) {
            this.lastTimeActive = lastTimeActive;
        }

        public void heartbeat() {
            lastTimeActive = new DateTime();
        }

        public String getServiceId() {
            return serviceId;
        }

        public boolean hasTimeout() {
            return Seconds.secondsBetween(new DateTime(), getLastTimeActive()).isGreaterThan(Seconds.parseSeconds("60"));
        }
    }

    class AppRuntimeMetadata extends RuntimeMetadataBase {

        private String instanceId;

        AppRuntimeMetadata(String serviceId, String instanceId) {
            super(serviceId);
            this.instanceId = instanceId;
        }

        public String getInstanceId() {
            return instanceId;
        }

        public void setInstanceId(String instanceId) {
            this.instanceId = instanceId;
        }

        public boolean isIgnored() {
            return StringUtils.isEmpty(instanceId);
        }
    }

    class FogCellMetadata extends RuntimeMetadataBase {

        private final FogIdentification fogIdentification;

        FogCellMetadata(String serviceId, FogIdentification fogIdentification) {
            super(serviceId);
            this.fogIdentification = fogIdentification;
        }
    }
}
