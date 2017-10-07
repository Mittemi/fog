package at.sintrum.fog.simulation.api;

import at.sintrum.fog.simulation.api.dto.AppEventInfo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Michael Mittermayr on 17.07.2017.
 */
@RequestMapping(value = "simulation")
public interface SimulationApi {

//    @RequestMapping(value = "next/{step}/{instanceId}", method = RequestMethod.POST)
//    void nextStep(@PathVariable("instanceId") String instanceId, @PathVariable("step") String step);

    @RequestMapping(value = "starting/{instanceId}", method = RequestMethod.POST)
    void starting(@PathVariable("instanceId") String instanceId, @RequestBody AppEventInfo appEventInfo);

    @RequestMapping(value = "started/{instanceId}", method = RequestMethod.POST)
    void started(@PathVariable("instanceId") String instanceId, @RequestBody AppEventInfo appEventInfo);

    @RequestMapping(value = "recovered/{instanceId}", method = RequestMethod.POST)
    void recovered(@PathVariable("instanceId") String instanceId, @RequestBody AppEventInfo appEventInfo);

    @RequestMapping(value = "upgrading/{instanceId}", method = RequestMethod.POST)
    void upgrading(@PathVariable("instanceId") String instanceId, @RequestBody AppEventInfo appEventInfo);

    @RequestMapping(value = "upgraded/{instanceId}", method = RequestMethod.POST)
    void upgraded(@PathVariable("instanceId") String instanceId, @RequestBody AppEventInfo appEventInfo);

    @RequestMapping(value = "moving/{instanceId}", method = RequestMethod.POST)
    void moving(@PathVariable("instanceId") String instanceId, @RequestBody AppEventInfo appEventInfo);

    @RequestMapping(value = "moved/{instanceId}", method = RequestMethod.POST)
    void moved(@PathVariable("instanceId") String instanceId, @RequestBody AppEventInfo appEventInfo);

    @RequestMapping(value = "standby/{instanceId}", method = RequestMethod.POST)
    void standby(@PathVariable("instanceId") String instanceId, @RequestBody AppEventInfo appEventInfo);

    @RequestMapping(value = "teardown/{instanceId}", method = RequestMethod.POST)
    void teardown(@PathVariable("instanceId") String instanceId, @RequestBody AppEventInfo appEventInfo);
}
