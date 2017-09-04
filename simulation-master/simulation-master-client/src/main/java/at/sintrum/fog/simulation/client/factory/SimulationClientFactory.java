package at.sintrum.fog.simulation.client.factory;

import at.sintrum.fog.clientcore.annotation.EnableRetry;
import at.sintrum.fog.clientcore.client.ClientFactory;
import at.sintrum.fog.simulation.client.api.FogCellStateClient;
import at.sintrum.fog.simulation.client.api.FogResourcesClient;
import at.sintrum.fog.simulation.client.api.SimulationClient;

/**
 * Created by Michael Mittermayr on 08.08.2017.
 */
public interface SimulationClientFactory extends ClientFactory {

    @EnableRetry
    SimulationClient createSimulationClient(String url);

    @EnableRetry
    FogResourcesClient createFogResourcesClient(String url);

    @EnableRetry
    FogCellStateClient createFogCellStateClient(String url);
}
