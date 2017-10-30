package at.sintrum.fog.simulation.api;

import at.sintrum.fog.core.dto.FogIdentification;
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

        if (basicScenarioInfo == null || basicScenarioInfo.getCloud() == null) {
            basicScenarioInfo = new BasicScenarioInfo();
            basicScenarioInfo.setIterations(1);
            basicScenarioInfo.setCloud(FogIdentification.parseFogBaseUrl("192.168.1.21:28080"));
            basicScenarioInfo.setFogA(FogIdentification.parseFogBaseUrl("192.168.1.101:28080"));
            basicScenarioInfo.setFogB(FogIdentification.parseFogBaseUrl("192.168.1.102:28080"));
            basicScenarioInfo.setFogC(FogIdentification.parseFogBaseUrl("192.168.1.103:28080"));
        }

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
