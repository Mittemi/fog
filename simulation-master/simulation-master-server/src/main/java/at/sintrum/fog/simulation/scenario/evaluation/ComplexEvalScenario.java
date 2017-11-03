package at.sintrum.fog.simulation.scenario.evaluation;

import at.sintrum.fog.applicationhousing.client.api.AppEvolutionClient;
import at.sintrum.fog.metadatamanager.api.dto.DockerImageMetadata;
import at.sintrum.fog.metadatamanager.client.api.AppRequestClient;
import at.sintrum.fog.metadatamanager.client.api.ImageMetadataClient;
import at.sintrum.fog.simulation.SimulationServerConfig;
import at.sintrum.fog.simulation.scenario.dto.BasicScenarioInfo;
import at.sintrum.fog.simulation.service.FogResourceService;
import at.sintrum.fog.simulation.service.ScenarioService;
import at.sintrum.fog.simulation.simulation.mongo.respositories.FullSimulationResultRepository;
import at.sintrum.fog.simulation.taskengine.TaskListBuilder;
import at.sintrum.fog.simulation.taskengine.TrackExecutionState;
import at.sintrum.fog.simulation.taskengine.tasks.WaitTillFinishedTask;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael Mittermayr on 02.11.2017.
 */
@Service
public class ComplexEvalScenario extends EvaluationScenarioBase {

    protected ComplexEvalScenario(TaskListBuilder taskListBuilder,
                                  ImageMetadataClient imageMetadataClient,
                                  SimulationServerConfig config,
                                  FogResourceService fogResourceService,
                                  AppRequestClient appRequestClient,
                                  AppEvolutionClient appEvolutionClient,
                                  FullSimulationResultRepository fullSimulationResultRepository,
                                  @Lazy ScenarioService scenarioService) {
        super(taskListBuilder,
                imageMetadataClient,
                config,
                fogResourceService,
                appRequestClient,
                appEvolutionClient,
                fullSimulationResultRepository,
                scenarioService,
                10,
                20);
    }

    @Override
    protected int[][] getRequestMatrix() {
        return BasicEvalScenario.getBasicEvalRequestMatrix();
    }

    @Override
    protected void setupSimulation(WaitTillFinishedTask.State simulationState, TaskListBuilder.TaskListBuilderState taskListBuilderState, BasicScenarioInfo basicScenarioInfo, List<TrackExecutionState> applications, boolean useAuction, ArrayList<TaskListBuilder.TaskListBuilderState.AppTaskBuilder> taskBuilders) {


        DockerImageMetadata firstAppV1 = getApplications().get(0);
        DockerImageMetadata firstAppV2 = createImageMetadata(firstAppV1.getApplicationName(), firstAppV1.getPorts().get(0), firstAppV1.isEnableDebugging(), true);

        DockerImageMetadata thirdAppV1 = getApplications().get(2);
        DockerImageMetadata thirdAppV2 = createImageMetadata(thirdAppV1.getApplicationName(), thirdAppV1.getPorts().get(0), thirdAppV1.isEnableDebugging(), true);


        // Network connectivity control track
        taskListBuilderState.createTrack()
                .logMessage(0, "Fog online state control track")
                .codedTask(120, () -> checkIfAllAppsRunning(simulationState))

                .logMessage(0, "Everything online for 3 min")
                .sleep(180)

                // FOG E (2 min)
                .logMessage(0, "Take Fog E offline")
                .setFogNetworkState(0, basicScenarioInfo.getFogE(), false, false)
                .logMessage(120, "Bring Fog E back online")
                .setFogNetworkState(0, basicScenarioInfo.getFogE(), true, false)

                .logMessage(0, "Everything online for 10 min")
                .sleep(600)

                // FOG B
                .logMessage(0, "Take Fog B offline")
                .setFogNetworkState(0, basicScenarioInfo.getFogB(), false, false)
                .logMessage(360, "Bring Fog B back online")
                .setFogNetworkState(0, basicScenarioInfo.getFogB(), true, false)

                // Track finished
                .logMessage(0, "Everything online again.");

        // App upgrade control track
        taskListBuilderState.createTrack()
                .logMessage(0, "App upgrade control track")
                .codedTask(120, () -> checkIfAllAppsRunning(simulationState))

                .logMessage(0, "First upgrade in 4 min")
                .sleep(240)

                .logMessage(0, "Upgrade app 1")
                .upgradeApp(0, firstAppV1, firstAppV2)

                .sleep(420)
                .logMessage(0, "Upgrade app 3")
                .upgradeApp(0, thirdAppV2, thirdAppV2)

                .logMessage(0, "App upgrades completed");
    }

    @Override
    public String getId() {
        return "complexEval";
    }
}
