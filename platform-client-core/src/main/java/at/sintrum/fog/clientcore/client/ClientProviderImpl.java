package at.sintrum.fog.clientcore.client;

import feign.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Michael Mittermayr on 27.05.2017.
 */
public class ClientProviderImpl implements at.sintrum.fog.clientcore.client.ClientProvider {

    private DiscoveryClient discoveryClient;
    private Client eurekaEnabledClient;
    private Client basicClient;

    private Logger LOG = LoggerFactory.getLogger(ClientProviderImpl.class);

    private static final String[] KNOWN_APPLICATIONS = {"deployment-manager", "metadata-manager", "application-housing", "simulation-master"};


    public ClientProviderImpl(DiscoveryClient discoveryClient, Client eurekaEnabledClient, Client basicClient) {
        this.discoveryClient = discoveryClient;
        this.eurekaEnabledClient = eurekaEnabledClient;
        this.basicClient = basicClient;
    }


    @Override
    public Client getClient(String url) {
        if (discoveryClient == null) {
            throw new RuntimeException("ClientProviderImpl not initialized");
        }

        if (Arrays.stream(KNOWN_APPLICATIONS).anyMatch(url::contains)) {
            LOG.info("Use Eureka Client for known application: " + url);
            return eurekaEnabledClient;
        }

        List<String> services = discoveryClient.getServices();
        if (services.stream().anyMatch(url::contains)) {
            LOG.info("Use Eureka Client for dynamically registered application: " + url);
            return eurekaEnabledClient;
        }
        LOG.info("Use Basic Client for: " + url);
        return basicClient;
    }
}
