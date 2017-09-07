package at.sintrum.fog.simulation.api;

import at.sintrum.fog.simulation.scenario.dto.BasicScenarioInfo;
import at.sintrum.fog.simulation.scenario.dto.ScenarioExecutionInfo;
import at.sintrum.fog.simulation.service.ScenarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Michael Mittermayr on 24.08.2017.
 */
@RestController
@RequestMapping("scenario")
public class ScenarioController {

    private final ScenarioService scenarioService;

    public ScenarioController(ScenarioService scenarioService) {
        this.scenarioService = scenarioService;
    }

    @RequestMapping(value = "run/{name}", method = RequestMethod.POST)
    public ScenarioExecutionInfo runScenario(@PathVariable("name") String name, @RequestBody BasicScenarioInfo basicScenarioInfo) {
        return scenarioService.run(basicScenarioInfo, name);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<String> getNames() {
        return scenarioService.getScenarioNames();
    }

    @RequestMapping(value = "state", method = RequestMethod.GET)
    public ScenarioExecutionInfo getState() {
        return scenarioService.getExecutionState();
    }

    @RequestMapping(value = "cancel", method = RequestMethod.DELETE)
    public boolean cancel() {
        return scenarioService.cancel();
    }
}
