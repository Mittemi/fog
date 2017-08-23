package at.sintrum.fog.applicationhousing.service;

import at.sintrum.fog.metadatamanager.api.ApplicationStateMetadataApi;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Michael Mittermayr on 21.07.2017.
 */
@Service
public class ApplicationRecoveryImpl {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationRecoveryImpl.class);

    private boolean isCheckRunning = false;
    private DiscoveryClient discoveryClient;
    private final ApplicationStateMetadataApi applicationStateMetadataApi;

    private final Map<String, AppRuntimeMetadata> appRuntimeMetadata;

    public ApplicationRecoveryImpl(DiscoveryClient discoveryClient, ApplicationStateMetadataApi applicationStateMetadataApi) {
        this.discoveryClient = discoveryClient;
        this.applicationStateMetadataApi = applicationStateMetadataApi;
        appRuntimeMetadata = new HashMap<>();
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
        List<String> knownServices = discoveryClient.getServices();

        for (String serviceId : knownServices) {
            if (!appRuntimeMetadata.containsKey(serviceId)) {
                appRuntimeMetadata.put(serviceId, new AppRuntimeMetadata());
            }
        }

        for (String serviceId : appRuntimeMetadata.keySet()) {
            List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);

            for (ServiceInstance instance : instances) {
                Map<String, String> metadata = instance.getMetadata();
            }

            AppRuntimeMetadata appRuntimeMetadata = this.appRuntimeMetadata.get(serviceId);

            //TODO: ask app for health state


        }
    }

    class AppRuntimeMetadata {
        private DateTime timeAdded;
        private DateTime lastTimeActive;

        public AppRuntimeMetadata() {
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
    }
}
