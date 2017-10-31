package at.sintrum.fog.simulation.scenario;

import at.sintrum.fog.simulation.scenario.dto.BasicScenarioInfo;
import at.sintrum.fog.simulation.taskengine.TaskListBuilder;
import at.sintrum.fog.simulation.taskengine.TrackBuilderState;
import org.springframework.stereotype.Service;

/**
 * Created by Michael Mittermayr on 05.09.2017.
 */
@Service
public class RecoverInCloudScenario implements Scenario {

    private final TaskListBuilder taskListBuilder;

    public RecoverInCloudScenario(TaskListBuilder taskListBuilder) {
        this.taskListBuilder = taskListBuilder;
    }

    @Override
    public String getId() {
        return "recoverInCloud";
    }

    @Override
    public TaskListBuilder.TaskListBuilderState build(BasicScenarioInfo basicScenarioInfo, boolean useAuction) {

        TaskListBuilder.TaskListBuilderState taskListBuilderState = taskListBuilder.newTaskList(this);

        TrackBuilderState state = new TrackBuilderState();

        taskListBuilderState.createTrack()
                .resetMetadata(0)

                .startTestApp(0, basicScenarioInfo.getCloud())
                .requestApp(5, basicScenarioInfo.getFogA(), 10)
                .checkLocation(5, basicScenarioInfo.getFogA())
                .logMessage(0, "App now in fog")
                .setFogNetworkState(0, basicScenarioInfo.getFogA(), false, true)
                .updateScenarioState(0, state)                              //save state
                .stopAppContainer(0, basicScenarioInfo.getFogA())
                .logMessage(0, "Fog is now offline, wait for recovery in cloud")
                .updateInstanceId(10)
                .logMessage(0, "InstanceId has been updated")
                .checkLocation(60, basicScenarioInfo.getCloud())
                .checkReachability(0, true)
                .logMessage(0, "App has been recovered in cloud. Take fog online again")
                .setFogNetworkState(0, basicScenarioInfo.getFogA(), true, true)
                .startContainer(0, basicScenarioInfo.getFogA(), state)      // restore from old state
                .logMessage(0, "Fog back online, wait 120 seconds to see effects before shutdown")
                .logMessage(120, "Scenario finished")
                .removeApp(0);

        return taskListBuilderState;
    }
}
