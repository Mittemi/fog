package at.sintrum.fog.simulation.service;

import at.sintrum.fog.simulation.scenario.dto.BasicScenarioInfo;
import at.sintrum.fog.simulation.scenario.dto.ScenarioExecutionInfo;
import at.sintrum.fog.simulation.simulation.ScenarioExecutionResult;

import java.util.List;

/**
 * Created by Michael Mittermayr on 02.09.2017.
 */
public interface ScenarioService {

    List<String> getScenarioNames();

    ScenarioExecutionInfo run(BasicScenarioInfo basicScenarioInfo, String name);

    ScenarioExecutionInfo getExecutionState();

    ScenarioExecutionResult getResult();

    boolean cancel();
}
