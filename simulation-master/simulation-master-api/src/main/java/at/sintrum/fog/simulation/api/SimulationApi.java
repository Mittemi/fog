package at.sintrum.fog.simulation.api;

import at.sintrum.fog.core.dto.FogIdentification;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Michael Mittermayr on 17.07.2017.
 */
@RequestMapping(value = "simulation")
public interface SimulationApi {

    @RequestMapping(value = "notifyMove/{instanceId}", method = RequestMethod.POST)
    void notifyMove(@PathVariable("instanceId") String instanceId, @RequestBody FogIdentification targetDeploymentManager);

    @RequestMapping(value = "notifyStarting/{instanceId}", method = RequestMethod.POST)
    void notifyStarting(@PathVariable("instanceId") String instanceId, @RequestBody FogIdentification appIdentification);

    @RequestMapping(value = "notifyUpgrade/{instanceId}", method = RequestMethod.POST)
    void notifyUpgrade(@PathVariable("instanceId") String instanceId, @RequestBody FogIdentification targetCloud);

    @RequestMapping(value = "notifyStandby/{instanceId}", method = RequestMethod.POST)
    void notifyStandby(@PathVariable("instanceId") String instanceId, @RequestBody FogIdentification appIdentification);

    @RequestMapping(value = "notifyRunning/{instanceId}", method = RequestMethod.POST)
    void notifyRunning(@PathVariable("instanceId") String instanceId, @RequestBody FogIdentification appIdentification);

    @RequestMapping(value = "heartbeat/{instanceId}", method = RequestMethod.POST)
    void sendHeartbeat(@PathVariable("instanceId") String instanceId);

    @RequestMapping(value = "notifyMoved/{instanceId}", method = RequestMethod.POST)
    void notifyMoved(@PathVariable("instanceId") String instanceId, @RequestBody FogIdentification appIdentification);
}
