package at.sintrum.fog.simulation.scenario;

import at.sintrum.fog.metadatamanager.client.api.AppRequestClient;
import at.sintrum.fog.simulation.scenario.dto.BasicScenarioInfo;
import at.sintrum.fog.simulation.taskengine.TaskListBuilder;
import org.springframework.stereotype.Service;

/**
 * Created by Michael Mittermayr on 11.01.2018.
 */
@Service
public class RestScenario implements Scenario {
    private final TaskListBuilder taskListBuilder;
    private final AppRequestClient appRequestClient;

    public RestScenario(TaskListBuilder taskListBuilder, AppRequestClient appRequestClient) {
        this.taskListBuilder = taskListBuilder;
        this.appRequestClient = appRequestClient;
    }

    @Override
    public String getId() {
        return "reset";
    }

    @Override
    public TaskListBuilder.TaskListBuilderState build(BasicScenarioInfo basicScenarioInfo, boolean useAuction) {
        TaskListBuilder.TaskListBuilderState taskListBuilderState = taskListBuilder.newTaskList(this, basicScenarioInfo);

        taskListBuilderState.resetMetadata();
        return taskListBuilderState;
    }
}
