package at.sintrum.fog.simulation.service;

import at.sintrum.fog.simulation.model.SimulationState;

/**
 * Created by Michael Mittermayr on 08.08.2017.
 */
public interface SimulationService {

    void processOperation(String instanceId, SimulationState state);

    void heartbeat(String instanceId);
}
