package at.sintrum.fog.application.core.service;

import at.sintrum.fog.core.dto.FogIdentification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by Michael Mittermayr on 27.07.2017.
 */
@Service
public class CloudLocatorServiceImpl implements CloudLocatorService {

    private final DiscoveryClient discoveryClient;
    private static final Logger LOG = LoggerFactory.getLogger(CloudLocatorServiceImpl.class);

    public CloudLocatorServiceImpl(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Override
    public String getCloudBaseUrl() {

        try {
            List<ServiceInstance> instances = discoveryClient.getInstances("deployment-manager");

            for (ServiceInstance instance : instances) {
                String profiles = instance.getMetadata().get("profiles");
                if (StringUtils.isEmpty(profiles)) {
                    continue;
                }
                if (profiles.contains("cloud")) {
                    String url = new FogIdentification(instance.getHost(), instance.getPort()).toUrl();
                    LOG.debug("Cloud url: " + url);
                    return url;
                }
            }
            LOG.warn("Unable to locate cloud!");
        } catch (Exception ex) {
            LOG.warn("Unable to locate cloud!", ex);
        }
        return null;
    }
}
