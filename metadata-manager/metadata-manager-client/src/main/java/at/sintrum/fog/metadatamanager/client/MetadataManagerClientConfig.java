package at.sintrum.fog.metadatamanager.client;

import at.sintrum.fog.clientcore.ClientCoreConfig;
import at.sintrum.fog.clientcore.client.ClientFactoryFactory;
import at.sintrum.fog.metadatamanager.client.api.ImageMetadata;
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
    public ImageMetadata applicationMetadata(MetadataManagerClientFactory clientFactory) {
        return clientFactory.createApplicationMetadata(null);
    }
}
