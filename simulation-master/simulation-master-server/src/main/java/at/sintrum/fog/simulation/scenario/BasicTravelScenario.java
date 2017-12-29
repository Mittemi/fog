package at.sintrum.fog.simulation.scenario;

import at.sintrum.fog.metadatamanager.client.api.AppRequestClient;
import at.sintrum.fog.simulation.scenario.dto.BasicScenarioInfo;
import at.sintrum.fog.simulation.taskengine.AppRequestState;
import at.sintrum.fog.simulation.taskengine.TaskListBuilder;
import org.springframework.stereotype.Service;

/**
 * Created by Michael Mittermayr on 02.09.2017.
 */
@Service
public class BasicTravelScenario implements Scenario {

    private final TaskListBuilder taskListBuilder;
    private final AppRequestClient appRequestClient;

    public BasicTravelScenario(TaskListBuilder taskListBuilder, AppRequestClient appRequestClient) {
        this.taskListBuilder = taskListBuilder;
        this.appRequestClient = appRequestClient;
    }

    @Override
    public String getId() {
        return "basicTravel";
    }

    @Override
    public TaskListBuilder.TaskListBuilderState build(BasicScenarioInfo basicScenarioInfo, boolean useAuction) {

        if (useAuction) {
            appRequestClient.enableAuction();
        } else {
            appRequestClient.disableAuction();
        }

        TaskListBuilder.TaskListBuilderState taskListBuilderState = taskListBuilder.newTaskList(this, basicScenarioInfo);

        AppRequestState fogAState = new AppRequestState();
        AppRequestState fogBState = new AppRequestState();
        AppRequestState fogCState = new AppRequestState();
        AppRequestState fogDState = new AppRequestState();
        AppRequestState fogEState = new AppRequestState();

        taskListBuilderState.createTrack()
                .resetMetadata(0)
                .startTestApp(0, basicScenarioInfo.getCloud())
                //.checkLocation(10, basicScenarioInfo.getCloud())
                .logMessage(0, "Requests for app")
                .requestApp(0, basicScenarioInfo.getFogA(), 10, fogAState, 10)
                .requestApp(0, basicScenarioInfo.getFogB(), 10, fogBState, 9)
                .requestApp(0, basicScenarioInfo.getFogC(), 10, fogCState, 8)
                .requestApp(0, basicScenarioInfo.getFogD(), 10, fogDState, 7)
                .requestApp(0, basicScenarioInfo.getFogE(), 10, fogEState, 6)
                .waitForAppToFinishRequest(0, fogAState)
                .logMessage(0, "Fog a finished")
                .waitForAppToFinishRequest(0, fogBState)
                .logMessage(0, "Fog b finished")
                .waitForAppToFinishRequest(0, fogCState)
                .logMessage(0, "Fog c finished")
                .waitForAppToFinishRequest(0, fogDState)
                .logMessage(0, "Fog d finished")
                .waitForAppToFinishRequest(0, fogEState)
                .logMessage(0, "Fog e finished")
                .checkLocation(0, basicScenarioInfo.getCloud())
                .logMessage(0, "App back in cloud")
                //.checkLocation(10, basicScenarioInfo.getFogA())
                //.finishWork(0)
                //.checkLocation(10, basicScenarioInfo.getCloud())
                .removeApp(0)
                .logMessage(0, "Track finished");

        return taskListBuilderState;
    }
}
