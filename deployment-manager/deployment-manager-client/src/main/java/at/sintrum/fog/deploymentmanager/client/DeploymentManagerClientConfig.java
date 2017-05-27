package at.sintrum.fog.deploymentmanager.client;

import at.sintrum.fog.clientcore.ClientCoreConfig;
import at.sintrum.fog.clientcore.client.ClientProvider;
import at.sintrum.fog.deploymentmanager.client.factory.DeploymentManagerClientFactory;
import at.sintrum.fog.deploymentmanager.client.factory.impl.FeignDeploymentManagerClientFactory;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.NoOpLoadBalancer;
import com.netflix.loadbalancer.Server;
import feign.Client;
import feign.Contract;
import feign.Request;
import feign.Response;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;

/**
 * Created by Michael Mittermayr on 17.05.2017.
 */
@Configuration
@Import({ClientCoreConfig.class})
public class DeploymentManagerClientConfig {

    @Bean
    public DeploymentManagerClientFactory deploymentManagerClientFactory(ClientProvider clientProvider, Decoder decoder, Encoder encoder, Contract contract) {
        return new FeignDeploymentManagerClientFactory(clientProvider, decoder, encoder, contract);
    }
}
