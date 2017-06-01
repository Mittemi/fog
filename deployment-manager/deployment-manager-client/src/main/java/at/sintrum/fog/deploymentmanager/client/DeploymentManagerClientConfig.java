package at.sintrum.fog.deploymentmanager.client;

import at.sintrum.fog.clientcore.ClientCoreConfig;
import at.sintrum.fog.clientcore.client.ClientFactoryFactory;
import at.sintrum.fog.deploymentmanager.client.factory.DeploymentManagerClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Michael Mittermayr on 17.05.2017.
 */
@Configuration
@Import({ClientCoreConfig.class})
public class DeploymentManagerClientConfig {

    private final Logger LOG = LoggerFactory.getLogger(DeploymentManagerClientConfig.class);

    //named bean required, don't know why but otherwise the creation order is mixed up
    @Bean(name = "DeploymentManagerClientFactory")
    public DeploymentManagerClientFactory deploymentManagerClientFactory(ClientFactoryFactory factory) {
        LOG.debug("Create DeploymentManagerClientFactory");
        return factory.buildFactory(DeploymentManagerClientFactory.class, "deployment-manager");
    }
}
