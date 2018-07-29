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
public class RecoveryEvalScenario extends EvaluationScenarioBase {

    protected RecoveryEvalScenario(TaskListBuilder taskListBuilder,
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
                120);
    }

    @Override
    protected int[][] getRequestMatrix() {
        return BasicEvalScenario.getBasicEvalRequestMatrix();
    }

    @Override
    protected void setupSimulation(WaitTillFinishedTask.State simulationState, TaskListBuilder.TaskListBuilderState taskListBuilderState, BasicScenarioInfo basicScenarioInfo, List<TrackExecutionState> applications, boolean useAuction, ArrayList<TaskListBuilder.TaskListBuilderState.AppTaskBuilder> taskBuilders) {

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
    }

    private DockerImageMetadata upgradeApp(DockerImageMetadata image) {
        return createImageMetadata(image.getApplicationName(), image.getPorts().get(0), image.isEnableDebugging(), true);
    }

    @Override
    public String getId() {
        return "recoveryEval";
    }
}
