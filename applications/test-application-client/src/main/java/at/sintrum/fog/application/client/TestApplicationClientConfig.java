package at.sintrum.fog.application.client;

import at.sintrum.fog.application.client.factory.TestApplicationClientFactory;
import at.sintrum.fog.clientcore.ClientCoreConfig;
import at.sintrum.fog.clientcore.client.ClientFactoryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Michael Mittermayr on 08.08.2017.
 */
@Configuration
@Import(ClientCoreConfig.class)
public class TestApplicationClientConfig {

    private final Logger LOG = LoggerFactory.getLogger(TestApplicationClientConfig.class);

    @Bean
    public TestApplicationClientFactory testApplicationClientFactory(ClientFactoryFactory factory) {
        LOG.debug("Create TestApplicationClientFactory");
        return factory.buildFactory(TestApplicationClientFactory.class, null);
    }
}
