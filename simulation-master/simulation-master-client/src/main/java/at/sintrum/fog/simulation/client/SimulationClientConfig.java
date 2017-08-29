package at.sintrum.fog.simulation.client;

import at.sintrum.fog.clientcore.ClientCoreConfig;
import at.sintrum.fog.clientcore.client.ClientFactoryFactory;
import at.sintrum.fog.simulation.api.FogResourcesApi;
import at.sintrum.fog.simulation.api.SimulationApi;
import at.sintrum.fog.simulation.client.api.SimulationClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Michael Mittermayr on 08.08.2017.
 */
@Configuration
@Import({ClientCoreConfig.class})
public class SimulationClientConfig {

    private final Logger LOG = LoggerFactory.getLogger(SimulationClientConfig.class);

    @Bean
    public SimulationClientFactory testApplicationClientFactory(ClientFactoryFactory factory) {
        LOG.debug("Create SimulationClient");
        return factory.buildFactory(SimulationClientFactory.class, "simulation-master");
    }

    @Bean
    public SimulationApi simulationApiClient(SimulationClientFactory simulationClientFactory) {
        return simulationClientFactory.createSimulationClient(null);
    }

    @Bean
    public FogResourcesApi fogResourcesClient(SimulationClientFactory simulationClientFactory) {
        return simulationClientFactory.createFogResourcesClient(null);
    }
}
