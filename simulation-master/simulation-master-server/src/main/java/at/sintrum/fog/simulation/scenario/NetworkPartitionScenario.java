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
    public TaskListBuilder.TaskListBuilderState build(BasicScenarioInfo basicScenarioInfo, boolean useAuction) {
        TaskListBuilder.TaskListBuilderState taskListBuilderState = taskListBuilder.newTaskList(this);

        taskListBuilderState.createTrack()
                .resetMetadata(0)

                .startTestApp(0, basicScenarioInfo.getCloud())
                .checkLocation(10, basicScenarioInfo.getCloud())
                .logMessage(0, "Set fog network offline")
                .setFogNetworkState(0, basicScenarioInfo.getFogA(), false, true)
                .requestApp(0, basicScenarioInfo.getFogA(), 10)
                .logMessage(0, "App should stay in cloud")
                .checkLocation(30, basicScenarioInfo.getCloud())
                .logMessage(0, "App still in cloud, wait another 30 seconds")
                .checkLocation(60, basicScenarioInfo.getCloud())
                .setFogNetworkState(0, basicScenarioInfo.getFogA(), true, false)
                .logMessage(0, "App in cloud, take network back online, might take a minute to propagate")
                .checkLocation(30, basicScenarioInfo.getFogA())
                .logMessage(0, "App is now in the fog. Everything worked")
                .removeApp(0)
                .logMessage(0, "Track finished");

        return taskListBuilderState;
    }
}
