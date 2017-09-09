package at.sintrum.fog.simulation.api;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.simulation.model.SimulationState;
import at.sintrum.fog.simulation.service.SimulationService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Michael Mittermayr on 08.08.2017.
 */
@RestController
public class SimulationController implements SimulationApi {

    private final SimulationService simulationService;

    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @Override
    public void notifyMove(@PathVariable("instanceId") String instanceId, @RequestBody FogIdentification targetDeploymentManager) {
        simulationService.processOperation(instanceId, SimulationState.Moving);
    }

    @Override
    public void notifyStarting(@PathVariable("instanceId") String instanceId, @RequestBody FogIdentification appIdentification) {
        simulationService.processOperation(instanceId, SimulationState.Starting);
    }

    @Override
    public void notifyUpgrade(@PathVariable("instanceId") String instanceId, @RequestBody FogIdentification targetCloud) {
        simulationService.processOperation(instanceId, SimulationState.Upgrading);
    }

    @Override
    public void notifyStandby(@PathVariable("instanceId") String instanceId, @RequestBody FogIdentification appIdentification) {
        simulationService.processOperation(instanceId, SimulationState.Standby);
    }

    @Override
    public void notifyRunning(@PathVariable("instanceId") String instanceId, @RequestBody FogIdentification appIdentification) {
        simulationService.processOperation(instanceId, SimulationState.Working);
    }

    @Override
    public void sendHeartbeat(@PathVariable("instanceId") String instanceId) {
        simulationService.heartbeat(instanceId);
    }

    @Override
    public void notifyMoved(String instanceId, FogIdentification appIdentification) {

    }
}
