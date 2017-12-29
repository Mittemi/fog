package at.sintrum.fog.simulation.scenario;

import at.sintrum.fog.simulation.scenario.dto.BasicScenarioInfo;
import at.sintrum.fog.simulation.taskengine.TaskListBuilder;
import org.springframework.stereotype.Service;

/**
 * Created by Michael Mittermayr on 07.09.2017.
 */
@Service
public class MultipleFogsScenario implements Scenario {

    private final TaskListBuilder taskListBuilder;

    public MultipleFogsScenario(TaskListBuilder taskListBuilder) {
        this.taskListBuilder = taskListBuilder;
    }

    @Override
    public String getId() {
        return "multiFog";
    }

    @Override
    public TaskListBuilder.TaskListBuilderState build(BasicScenarioInfo basicScenarioInfo, boolean useAuction) {

        TaskListBuilder.TaskListBuilderState taskListBuilderState = taskListBuilder.newTaskList(this, basicScenarioInfo);

        TaskListBuilder.TaskListBuilderState.AppTaskBuilder track = taskListBuilderState.resetMetadata()
                .createTrack();

        track.resetMetadata(0)
                .startTestApp(0, basicScenarioInfo.getCloud())
                .requestApp(0, basicScenarioInfo.getFogA(), 10);

        if (basicScenarioInfo.getFogB() != null)
            track.requestApp(0, basicScenarioInfo.getFogB(), 10);
        if (basicScenarioInfo.getFogC() != null)
            track.requestApp(0, basicScenarioInfo.getFogC(), 10);

        track.checkLocation(0, basicScenarioInfo.getFogA())
                .finishWork(0);

        if (basicScenarioInfo.getFogB() != null)
            track.checkLocation(0, basicScenarioInfo.getFogB())
                    .finishWork(0);

        if (basicScenarioInfo.getFogC() != null)
            track.checkLocation(0, basicScenarioInfo.getFogC())
                    .finishWork(0);

        track.checkLocation(0, basicScenarioInfo.getCloud())
                .removeApp(0);

        return taskListBuilderState;
    }
}
