package at.sintrum.fog.deploymentmanager.client;

import at.sintrum.fog.clientcore.ClientCoreConfig;
import at.sintrum.fog.deploymentmanager.client.factory.DeploymentManagerClientFactory;
import at.sintrum.fog.deploymentmanager.client.factory.impl.FeignDeploymentManagerClientFactory;
import feign.Client;
import feign.Contract;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Michael Mittermayr on 17.05.2017.
 */
@Configuration
@EnableFeignClients
@Import({ClientCoreConfig.class, FeignClientsConfiguration.class})
public class DeploymentManagerClientConfig {

    @Bean
    public DeploymentManagerClientFactory deploymentManagerClientFactory(Decoder decoder, Encoder encoder, Client client, Contract contract) {
        return new FeignDeploymentManagerClientFactory(decoder, encoder, client, contract);
    }

}
