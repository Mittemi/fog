package at.sintrum.fog.application.client;

import at.sintrum.fog.clientcore.ClientCoreConfig;
import at.sintrum.fog.clientcore.client.ClientFactoryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Michael Mittermayr on 23.08.2017.
 */
@Configuration
@Import({ClientCoreConfig.class})
public class ApplicationClientConfig {

    @Bean
    public ApplicationClientFactory applicationClientFactory(ClientFactoryFactory clientFactoryFactory) {
        return clientFactoryFactory.buildFactory(ApplicationClientFactory.class, null);
    }
}
