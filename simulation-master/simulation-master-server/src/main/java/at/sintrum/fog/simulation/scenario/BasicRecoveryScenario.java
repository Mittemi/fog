package at.sintrum.fog.simulation.scenario;

import at.sintrum.fog.simulation.scenario.dto.BasicScenarioInfo;
import at.sintrum.fog.simulation.taskengine.TaskListBuilder;
import org.springframework.stereotype.Service;

/**
 * Created by Michael Mittermayr on 04.09.2017.
 */
@Service
public class BasicRecoveryScenario implements Scenario {

    private final TaskListBuilder taskListBuilder;

    public BasicRecoveryScenario(TaskListBuilder taskListBuilder) {
        this.taskListBuilder = taskListBuilder;
    }

    @Override
    public String getId() {
        return "basicRecovery";
    }

    @Override
    public TaskListBuilder.TaskListBuilderState build(BasicScenarioInfo basicScenarioInfo) {
        TaskListBuilder.TaskListBuilderState taskListBuilderState = taskListBuilder.newTaskList(this);

        taskListBuilderState.createTrack()
                .resetMetadata(0)

                .startTestApp(0, basicScenarioInfo.getCloud())
                .checkLocation(10, basicScenarioInfo.getCloud())
                .stopAppContainer(0, basicScenarioInfo.getCloud())
                .logMessage(10, "Wait for recovery of container")
                .checkReachability(10, true)
                .checkLocation(2, basicScenarioInfo.getCloud())
                .logMessage(0, "Container recovery in cloud")
                .requestApp(0, basicScenarioInfo.getFogA())
                .checkLocation(10, basicScenarioInfo.getFogA())
                .stopAppContainer(0, basicScenarioInfo.getFogA())
                .checkReachability(10, true)
                .logMessage(0, "Container now in fog, wait for recovery")
                .checkLocation(0, basicScenarioInfo.getFogA())
                .removeApp(0)
                .logMessage(0, "Basic recovery scenario finished");


        return taskListBuilderState;
    }
}
