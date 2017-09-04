package at.sintrum.fog.simulation.client;

import at.sintrum.fog.clientcore.ClientCoreConfig;
import at.sintrum.fog.clientcore.client.ClientFactoryFactory;
import at.sintrum.fog.simulation.client.api.FogCellStateClient;
import at.sintrum.fog.simulation.client.api.FogResourcesClient;
import at.sintrum.fog.simulation.client.api.SimulationClient;
import at.sintrum.fog.simulation.client.factory.SimulationClientFactory;
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

    public SimulationClientConfig() {
    }

    @Bean
    public SimulationClientFactory testApplicationClientFactory(ClientFactoryFactory factory) {
        LOG.debug("Create SimulationClient");
        return factory.buildFactory(SimulationClientFactory.class, "simulation-master");
    }

    @Bean
    public SimulationClient simulationApiClient(SimulationClientFactory simulationClientFactory) {
        return simulationClientFactory.createSimulationClient(null);
    }

    @Bean
    public FogResourcesClient fogResourcesClient(SimulationClientFactory simulationClientFactory) {
        return simulationClientFactory.createFogResourcesClient(null);
    }

    @Bean
    public FogCellStateClient fogCellStateClient(SimulationClientFactory simulationClientFactory) {
        return simulationClientFactory.createFogCellStateClient(null);
    }
}
