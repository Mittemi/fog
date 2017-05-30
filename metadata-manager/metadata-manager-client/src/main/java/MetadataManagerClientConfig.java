import at.sintrum.fog.clientcore.ClientCoreConfig;
import at.sintrum.fog.clientcore.client.ClientProvider;
import factory.MetadataManagerClientFactory;
import factory.impl.FeignMetadataManagerClientFactory;
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

    @Bean
    public MetadataManagerClientFactory deploymentManagerClientFactory(ClientProvider clientProvider, Decoder decoder, Encoder encoder, Contract contract) {
        return new FeignMetadataManagerClientFactory(clientProvider, decoder, encoder, contract);
    }
}
