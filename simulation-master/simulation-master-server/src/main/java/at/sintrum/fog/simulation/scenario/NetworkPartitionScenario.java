package at.sintrum.fog.simulation.scenario;

import at.sintrum.fog.simulation.scenario.dto.BasicScenarioInfo;
import at.sintrum.fog.simulation.taskengine.TaskListBuilder;
import org.springframework.stereotype.Service;

/**
 * Created by Michael Mittermayr on 04.09.2017.
 */
@Service
public class NetworkPartitionScenario implements Scenario {

    private final TaskListBuilder taskListBuilder;

    public NetworkPartitionScenario(TaskListBuilder taskListBuilder) {
        this.taskListBuilder = taskListBuilder;
    }

    @Override
    public String getId() {
        return "networkPartition";
    }

    @Override
    public TaskListBuilder.TaskListBuilderState build(BasicScenarioInfo basicScenarioInfo) {
        TaskListBuilder.TaskListBuilderState taskListBuilderState = taskListBuilder.newTaskList(this);

        taskListBuilderState.createTrack()
                .resetMetadata(0)

                .startTestApp(0, basicScenarioInfo.getCloud())
                .checkLocation(10, basicScenarioInfo.getCloud())


                .removeApp(0)
                .logMessage(0, "Track finished");

        return taskListBuilderState;
    }
}
