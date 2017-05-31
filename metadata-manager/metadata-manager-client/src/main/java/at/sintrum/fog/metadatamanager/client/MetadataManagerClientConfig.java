package at.sintrum.fog.metadatamanager.client;

import at.sintrum.fog.clientcore.ClientCoreConfig;
import at.sintrum.fog.clientcore.client.ClientProvider;
import at.sintrum.fog.metadatamanager.client.api.ApplicationMetadata;
import at.sintrum.fog.metadatamanager.client.factory.MetadataManagerClientFactory;
import at.sintrum.fog.metadatamanager.client.factory.impl.FeignMetadataManagerClientFactory;
import feign.Contract;
import feign.codec.Decoder;
import feign.codec.Encoder;
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
    public MetadataManagerClientFactory deploymentManagerClientFactory(ClientProvider clientProvider, Decoder decoder, Encoder encoder, Contract contract) {
        return new FeignMetadataManagerClientFactory(clientProvider, decoder, encoder, contract);
    }

    @Bean
    public ApplicationMetadata applicationMetadata(MetadataManagerClientFactory clientFactory) {
        return clientFactory.createApplicationMetadata(null);
    }
}
