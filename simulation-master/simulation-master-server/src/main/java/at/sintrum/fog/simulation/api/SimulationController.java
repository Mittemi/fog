package at.sintrum.fog.simulation.api;

import at.sintrum.fog.simulation.api.dto.AppEventInfo;
import at.sintrum.fog.simulation.service.SimulationService;
import at.sintrum.fog.simulation.simulation.AppEvent;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Michael Mittermayr on 08.08.2017.
 */
@RestController
public class SimulationController implements SimulationApi {

    private final SimulationService simulationService;

    private final Logger LOG = LoggerFactory.getLogger(SimulationController.class);

    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }


    @Override
    public void starting(@PathVariable("instanceId") String instanceId, @RequestBody AppEventInfo appEventInfo) {
        LOG.debug("Starting: " + instanceId);
        simulationService.processOperation(instanceId, new AppEvent("starting", new DateTime(), appEventInfo.getNewLocation()), appEventInfo);
    }

    @Override
    public void started(@PathVariable("instanceId") String instanceId, @RequestBody AppEventInfo appEventInfo) {
        LOG.debug("Started: " + instanceId);
        simulationService.processOperation(instanceId, new AppEvent("started", new DateTime(), appEventInfo.getNewLocation()), appEventInfo);
    }

    @Override
    public void recovered(@PathVariable("instanceId") String instanceId, @RequestBody AppEventInfo appEventInfo) {
        LOG.debug("Recovered: " + instanceId);
        simulationService.processOperation(instanceId, new AppEvent("recovered", new DateTime(), appEventInfo.getNewLocation()), appEventInfo);
    }

    @Override
    public void upgrading(@PathVariable("instanceId") String instanceId, @RequestBody AppEventInfo appEventInfo) {
        LOG.debug("Upgrading: " + instanceId);
        simulationService.processOperation(instanceId, new AppEvent("upgrading", new DateTime(), appEventInfo.getNewLocation()), appEventInfo);
    }

    @Override
    public void upgraded(@PathVariable("instanceId") String instanceId, @RequestBody AppEventInfo appEventInfo) {
        LOG.debug("Upgraded: " + instanceId);
        simulationService.processOperation(instanceId, new AppEvent("upgraded", new DateTime(), appEventInfo.getNewLocation()), appEventInfo);
    }

    @Override
    public void moving(@PathVariable("instanceId") String instanceId, @RequestBody AppEventInfo appEventInfo) {
        LOG.debug("Moving: " + instanceId);
        simulationService.processOperation(instanceId, new AppEvent("moving", new DateTime(), appEventInfo.getNewLocation()), appEventInfo);
    }

    @Override
    public void moved(@PathVariable("instanceId") String instanceId, @RequestBody AppEventInfo appEventInfo) {
        LOG.debug("Moved: " + instanceId);
        simulationService.processOperation(instanceId, new AppEvent("moved", new DateTime(), appEventInfo.getNewLocation()), appEventInfo);
    }

    @Override
    public void standby(@PathVariable("instanceId") String instanceId, @RequestBody AppEventInfo appEventInfo) {
        LOG.debug("Standby: " + instanceId);
        simulationService.processOperation(instanceId, new AppEvent("standby", new DateTime(), appEventInfo.getNewLocation()), appEventInfo);
    }

    @Override
    public void teardown(@PathVariable("instanceId") String instanceId, @RequestBody AppEventInfo appEventInfo) {
        LOG.debug("Teardown: " + instanceId);
        simulationService.processOperation(instanceId, new AppEvent("teardown", new DateTime(), appEventInfo.getNewLocation()), appEventInfo);
    }
}