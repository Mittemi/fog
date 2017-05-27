package at.sintrum.fog.clientcore.client.impl;

import feign.Client;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.List;

/**
 * Created by Michael Mittermayr on 27.05.2017.
 */
public class ClientProvider implements at.sintrum.fog.clientcore.client.ClientProvider {

    private DiscoveryClient discoveryClient;
    private Client eurekaEnabledClient;
    private Client basicClient;

    public ClientProvider(DiscoveryClient discoveryClient, Client eurekaEnabledClient, Client basicClient) {
        this.discoveryClient = discoveryClient;
        this.eurekaEnabledClient = eurekaEnabledClient;
        this.basicClient = basicClient;
    }

    @Override
    public Client getClient(String url) {
        List<String> services = discoveryClient.getServices();
        if (services.stream().anyMatch(url::contains)) {
            return eurekaEnabledClient;
        }
        return basicClient;
    }
}
