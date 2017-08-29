package at.sintrum.fog.simulation.client.api;

import at.sintrum.fog.clientcore.annotation.EnableRetry;
import at.sintrum.fog.clientcore.client.ClientFactory;
import at.sintrum.fog.simulation.api.FogResourcesApi;
import at.sintrum.fog.simulation.api.SimulationApi;

/**
 * Created by Michael Mittermayr on 08.08.2017.
 */
public interface SimulationClientFactory extends ClientFactory {

    @EnableRetry
    SimulationApi createSimulationClient(String url);

    @EnableRetry
    FogResourcesApi createFogResourcesClient(String url);
}
