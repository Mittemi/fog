package at.sintrum.fog.deploymentmanager.client;

import at.sintrum.fog.clientcore.ClientCoreConfig;
import at.sintrum.fog.clientcore.client.ClientProvider;
import at.sintrum.fog.deploymentmanager.client.factory.DeploymentManagerClientFactory;
import at.sintrum.fog.deploymentmanager.client.factory.impl.FeignDeploymentManagerClientFactory;
import feign.Contract;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Michael Mittermayr on 17.05.2017.
 */
@Configuration
@Import({ClientCoreConfig.class})
public class DeploymentManagerClientConfig {

    //named bean required, don't know why but otherwise the creation order is mixed up
    @Bean(name = "DeploymentManagerClientFactory")
    public DeploymentManagerClientFactory deploymentManagerClientFactory(ClientProvider clientProvider, Decoder decoder, Encoder encoder, Contract contract) {
        return new FeignDeploymentManagerClientFactory(clientProvider, decoder, encoder, contract);
    }
}
