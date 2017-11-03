package at.sintrum.fog.simulation.scenario.evaluation;

import at.sintrum.fog.applicationhousing.client.api.AppEvolutionClient;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael Mittermayr on 31.10.2017.
 */
@Service
public class MiniEvalScenario extends EvaluationScenarioBase {

    private Logger LOG = LoggerFactory.getLogger(MiniEvalScenario.class);

    public MiniEvalScenario(TaskListBuilder taskListBuilder,
                            ImageMetadataClient imageMetadataClient,
                            SimulationServerConfig simulationServerConfig,
                            FogResourceService fogResourceService,
                            AppRequestClient appRequestClient,
                            AppEvolutionClient appEvolutionClient,
                            FullSimulationResultRepository fullSimulationResultRepository,
                            @Lazy ScenarioService scenarioService) {
        super(taskListBuilder,
                imageMetadataClient,
                simulationServerConfig,
                fogResourceService,
                appRequestClient,
                appEvolutionClient,
                fullSimulationResultRepository,
                scenarioService,
                5,
                20);
    }

    @Override
    protected int[][] getRequestMatrix() {
        return BasicEvalScenario.getBasicEvalRequestMatrix();
    }

    @Override
    protected void setupSimulation(WaitTillFinishedTask.State simulationControlTrack, TaskListBuilder.TaskListBuilderState taskListBuilderState, BasicScenarioInfo basicScenarioInfo, List<TrackExecutionState> applications, boolean useAuction, ArrayList<TaskListBuilder.TaskListBuilderState.AppTaskBuilder> taskBuilders) {
    }

    @Override
    public String getId() {
        return "miniEval";
    }
}
