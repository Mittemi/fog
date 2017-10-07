package at.sintrum.fog.simulation.api;

import at.sintrum.fog.simulation.api.dto.AppEventInfo;
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
    public void starting(@PathVariable("instanceId") String instanceId, @RequestBody AppEventInfo appEventInfo) {

    }

    @Override
    public void started(@PathVariable("instanceId") String instanceId, @RequestBody AppEventInfo appEventInfo) {

    }

    @Override
    public void recovered(@PathVariable("instanceId") String instanceId, @RequestBody AppEventInfo appEventInfo) {

    }

    @Override
    public void upgrading(@PathVariable("instanceId") String instanceId, @RequestBody AppEventInfo appEventInfo) {

    }

    @Override
    public void upgraded(@PathVariable("instanceId") String instanceId, @RequestBody AppEventInfo appEventInfo) {

    }

    @Override
    public void moving(@PathVariable("instanceId") String instanceId, @RequestBody AppEventInfo appEventInfo) {

    }

    @Override
    public void moved(@PathVariable("instanceId") String instanceId, @RequestBody AppEventInfo appEventInfo) {

    }

    @Override
    public void standby(@PathVariable("instanceId") String instanceId, @RequestBody AppEventInfo appEventInfo) {

    }

    @Override
    public void teardown(@PathVariable("instanceId") String instanceId, @RequestBody AppEventInfo appEventInfo) {

    }
}
