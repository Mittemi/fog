package at.sintrum.fog.simulation.scenario;

import at.sintrum.fog.core.dto.ResourceInfo;
import at.sintrum.fog.simulation.scenario.dto.BasicScenarioInfo;
import at.sintrum.fog.simulation.taskengine.TaskListBuilder;
import org.springframework.stereotype.Service;

/**
 * Created by Michael Mittermayr on 03.09.2017.
 */
@Service
public class ResourceLimitScenario implements Scenario {

    private final TaskListBuilder taskListBuilder;

    public ResourceLimitScenario(TaskListBuilder taskListBuilder) {
        this.taskListBuilder = taskListBuilder;
    }


    @Override
    public String getId() {
        return "resourceLimit";
    }

    @Override
    public TaskListBuilder.TaskListBuilderState build(BasicScenarioInfo basicScenarioInfo, boolean useAuction) {

        TaskListBuilder.TaskListBuilderState taskList = taskListBuilder.newTaskList(this, basicScenarioInfo);

        taskList.createTrack()
                .resetMetadata(0)
                .startTestApp(0, basicScenarioInfo.getCloud())
//                .requestApp(10, basicScenarioInfo.getFogA())
//                .checkLocation(5, basicScenarioInfo.getFogA())
//                .finishWork(0)
                .checkLocation(10, basicScenarioInfo.getCloud())
                .logMessage(0, "Set resource limits for fog")
                .setResourceLimit(0, basicScenarioInfo.getFogA(), ResourceInfo.fixedSized(0))
                .requestApp(0, basicScenarioInfo.getFogA(), 10)
                .checkLocation(60, basicScenarioInfo.getCloud())
                .logMessage(0, "Remove resource limits from fog")
                .setResourceLimit(0, basicScenarioInfo.getFogA(), null)
                .checkLocation(10, basicScenarioInfo.getFogA())
                .logMessage(0, "App traveled to fog. Finished")
                .removeApp(0);

        return taskList;
    }
}
