package at.sintrum.fog.applicationhousing.client;

import at.sintrum.fog.applicationhousing.client.api.AppEvolutionClient;
import at.sintrum.fog.applicationhousing.client.api.AppRecoveryClient;
import at.sintrum.fog.applicationhousing.client.client.AppHousingClientFactory;
import at.sintrum.fog.clientcore.ClientCoreConfig;
import at.sintrum.fog.clientcore.client.ClientFactoryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Michael Mittermayr on 14.07.2017.
 */
@Configuration
@Import(ClientCoreConfig.class)
public class ApplicationHousingClientConfig {

    private final Logger LOG = LoggerFactory.getLogger(ApplicationHousingClientConfig.class);

    @Bean
    public AppHousingClientFactory appHousingClientFactory(ClientFactoryFactory factory) {
        LOG.debug("Create AppHousingClientFactory");
        return factory.buildFactory(AppHousingClientFactory.class, "application-housing");
    }

    @Bean
    public AppEvolutionClient createAppEvolutionClient(AppHousingClientFactory clientFactory) {
        return clientFactory.createAppEvolutionClient(null);
    }

    @Bean
    public AppRecoveryClient createAppRecoveryClient(AppHousingClientFactory clientFactory) {
        return clientFactory.createRecoveryClient(null);
    }
}
