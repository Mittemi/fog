package at.sintrum.fog.metadatamanager.client;

import at.sintrum.fog.clientcore.ClientCoreConfig;
import at.sintrum.fog.clientcore.client.ClientFactoryFactory;
import at.sintrum.fog.metadatamanager.client.api.AppRequestClient;
import at.sintrum.fog.metadatamanager.client.api.ApplicationStateMetadataClient;
import at.sintrum.fog.metadatamanager.client.api.ContainerMetadataClient;
import at.sintrum.fog.metadatamanager.client.api.ImageMetadataClient;
import at.sintrum.fog.metadatamanager.client.factory.MetadataManagerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


/**
 * Created by Michael Mittermayr on 30.05.2017.
 */
@Configuration
@Import({ClientCoreConfig.class})
public class MetadataManagerClientConfig {

    //named bean required, don't know why but otherwise the creation order is mixed up
    @Bean(name = "MetadataManagerClientFactory")
    public MetadataManagerClientFactory deploymentManagerClientFactory(ClientFactoryFactory factory) {
        return factory.buildFactory(MetadataManagerClientFactory.class, "metadata-manager");
    }

    @Bean
    public ImageMetadataClient applicationMetadata(MetadataManagerClientFactory clientFactory) {
        return clientFactory.createApplicationMetadataClient(null);
    }

    @Bean
    public ContainerMetadataClient containerMetadataApi(MetadataManagerClientFactory clientFactory) {
        return clientFactory.createContainerMetadataClient(null);
    }

    @Bean
    public AppRequestClient appRequestClient(MetadataManagerClientFactory clientFactory) {
        return clientFactory.createAppRequestClient(null);
    }

    @Bean
    public ApplicationStateMetadataClient applicationStateMetadataApi(MetadataManagerClientFactory clientFactory) {
        return clientFactory.createApplicationStateMetadataClient(null);
    }
}
