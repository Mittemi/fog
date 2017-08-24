package at.sintrum.fog.simulation.api;

import at.sintrum.fog.simulation.taskengine.FogTaskList;
import at.sintrum.fog.simulation.taskengine.FogTaskRunner;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Michael Mittermayr on 24.08.2017.
 */
@RestController
@RequestMapping("scenario")
public class ScenarioController {

    private final FogTaskRunner taskRunner;
    private final FogTaskList taskList;

    public ScenarioController(FogTaskRunner taskRunner, FogTaskList taskList) {
        this.taskRunner = taskRunner;
        this.taskList = taskList;
    }

    @RequestMapping(value = "run", method = RequestMethod.POST)
    public void runScenario() {
        taskList.build();
        taskRunner.setTaskList(taskList);
    }
}
