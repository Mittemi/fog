package at.sintrum.fog.simulation.scenario;

import at.sintrum.fog.simulation.scenario.dto.BasicScenarioInfo;
import at.sintrum.fog.simulation.taskengine.TaskListBuilder;

/**
 * Created by Michael Mittermayr on 02.09.2017.
 */
public interface Scenario {

    String getId();

    TaskListBuilder.TaskListBuilderState build(BasicScenarioInfo basicScenarioInfo);
}
