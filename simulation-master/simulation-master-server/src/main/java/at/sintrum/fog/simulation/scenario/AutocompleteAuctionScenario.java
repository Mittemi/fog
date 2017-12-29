package at.sintrum.fog.simulation.scenario;

import at.sintrum.fog.simulation.scenario.dto.BasicScenarioInfo;
import at.sintrum.fog.simulation.taskengine.AppRequestState;
import at.sintrum.fog.simulation.taskengine.TaskListBuilder;
import org.springframework.stereotype.Service;

/**
 * Created by Michael Mittermayr on 30.10.2017.
 */
@Service
public class AutocompleteAuctionScenario implements Scenario {

    private final TaskListBuilder taskListBuilder;

    public AutocompleteAuctionScenario(TaskListBuilder taskListBuilder) {
        this.taskListBuilder = taskListBuilder;
    }


    @Override
    public String getId() {
        return "autocompleteAuction";
    }

    @Override
    public TaskListBuilder.TaskListBuilderState build(BasicScenarioInfo basicScenarioInfo, boolean useAuction) {
        TaskListBuilder.TaskListBuilderState taskListBuilderState = taskListBuilder.newTaskList(this, basicScenarioInfo);

        AppRequestState firstRequest = new AppRequestState();

        taskListBuilderState.createTrack()
                .resetMetadata(0)

                .startTestApp(0, basicScenarioInfo.getCloud())
                .checkLocation(10, basicScenarioInfo.getCloud())
                .requestApp(0, basicScenarioInfo.getFogA(), 5, firstRequest, 10)
                .logMessage(0, "Wait app to finish request")
                .waitForAppToFinishRequest(0, firstRequest)
                .logMessage(0, "Request finished")
                .checkLocation(0, basicScenarioInfo.getCloud())
                .logMessage(0, "Back in Cloud, start cleanup")
                .removeApp(0)
                .logMessage(0, "Autocomplete scenario with auctioning finished");


        return taskListBuilderState;
    }
}
