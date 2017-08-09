package at.sintrum.fog.simulation.appclient;

import at.sintrum.fog.clientcore.ClientCoreConfig;
import at.sintrum.fog.clientcore.client.ClientFactoryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Michael Mittermayr on 08.08.2017.
 */
@Configuration
@Import({ClientCoreConfig.class})
public class PlatformAppClientConfig {

    @Bean
    public PlatformAppClientFactory platformAppClientFactory(ClientFactoryFactory clientFactoryFactory) {
        return clientFactoryFactory.buildFactory(PlatformAppClientFactory.class, null);
    }
}
