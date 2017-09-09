package at.sintrum.fog.simulation.scenario;

import at.sintrum.fog.simulation.scenario.dto.BasicScenarioInfo;
import at.sintrum.fog.simulation.taskengine.TaskListBuilder;
import org.springframework.stereotype.Service;

/**
 * Created by Michael Mittermayr on 08.09.2017.
 */
@Service
public class MultiAppScenario implements Scenario {

    private final TaskListBuilder taskListBuilder;

    public MultiAppScenario(TaskListBuilder taskListBuilder) {
        this.taskListBuilder = taskListBuilder;
    }

    @Override
    public String getId() {
        return "multiApp";
    }

    @Override
    public TaskListBuilder.TaskListBuilderState build(BasicScenarioInfo basicScenarioInfo) {

        if (basicScenarioInfo.getIterations() == 0) {
            basicScenarioInfo.setIterations(1);
        }

        TaskListBuilder.TaskListBuilderState taskListBuilderState = taskListBuilder.newTaskList(this);

        taskListBuilderState.resetMetadata();

        TaskListBuilder.TaskListBuilderState.AppTaskBuilder trackA = taskListBuilderState.createTrack();
        TaskListBuilder.TaskListBuilderState.AppTaskBuilder trackB = taskListBuilderState.createTrack();

        for (int i = 0; i < basicScenarioInfo.getIterations(); i++) {
            trackA.startTestApp(0, basicScenarioInfo.getCloud())
                    .requestApp(0, basicScenarioInfo.getFogA())
                    .requestApp(0, basicScenarioInfo.getFogB())
                    .requestApp(0, basicScenarioInfo.getFogC())
                    .checkLocation(0, basicScenarioInfo.getFogA())
                    .finishWork(0)
                    .checkLocation(0, basicScenarioInfo.getFogB())
                    .finishWork(0)
                    .checkLocation(0, basicScenarioInfo.getFogC())
                    .finishWork(0)
                    .checkLocation(0, basicScenarioInfo.getCloud())
                    .logMessage(0, "Track A finished (" + i + ")")
                    .removeApp(0);

            trackB.startAnotherApp(0, basicScenarioInfo.getCloud())
                    .requestApp(0, basicScenarioInfo.getFogA())
                    .requestApp(0, basicScenarioInfo.getFogB())
                    .requestApp(0, basicScenarioInfo.getFogC())
                    .checkLocation(0, basicScenarioInfo.getFogA())
                    .finishWork(0)
                    .checkLocation(0, basicScenarioInfo.getFogB())
                    .finishWork(0)
                    .checkLocation(0, basicScenarioInfo.getFogC())
                    .finishWork(0)
                    .checkLocation(0, basicScenarioInfo.getCloud())
                    .logMessage(0, "Track B finished (" + i + ")")
                    .removeApp(0);
        }

        return taskListBuilderState;
    }
}
