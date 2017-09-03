package at.sintrum.fog.simulation.scenario;

import at.sintrum.fog.simulation.scenario.dto.BasicScenarioInfo;
import at.sintrum.fog.simulation.taskengine.TaskListBuilder;
import org.springframework.stereotype.Service;

/**
 * Created by Michael Mittermayr on 02.09.2017.
 */
@Service
public class BasicTravelScenario implements Scenario {

    private final TaskListBuilder taskListBuilder;

    public BasicTravelScenario(TaskListBuilder taskListBuilder) {
        this.taskListBuilder = taskListBuilder;
    }

    @Override
    public String getId() {
        return "basicTravel";
    }

    @Override
    public TaskListBuilder.TaskListBuilderState build(BasicScenarioInfo basicScenarioInfo) {
        TaskListBuilder.TaskListBuilderState taskListBuilderState = taskListBuilder.newTaskList();

        taskListBuilderState.createTrack()
                .resetMetadata(0)
                .startTestApp(0, basicScenarioInfo.getCloud())
                .checkLocation(10, basicScenarioInfo.getCloud())
                .requestApp(0, basicScenarioInfo.getFogA())
                .checkLocation(10, basicScenarioInfo.getFogA())
                .finishWork(0)
                .checkLocation(10, basicScenarioInfo.getCloud())
                .removeApp(0)
                .logMessage(0, "Track finished");

        return taskListBuilderState;
    }
}
