package at.sintrum.fog.simulation.scenario.evaluation;

import at.sintrum.fog.metadatamanager.client.api.AppRequestClient;
import at.sintrum.fog.metadatamanager.client.api.ImageMetadataClient;
import at.sintrum.fog.simulation.SimulationServerConfig;
import at.sintrum.fog.simulation.scenario.dto.BasicScenarioInfo;
import at.sintrum.fog.simulation.service.FogResourceService;
import at.sintrum.fog.simulation.taskengine.TaskListBuilder;
import at.sintrum.fog.simulation.taskengine.TrackExecutionState;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael Mittermayr on 02.11.2017.
 */
@Service
public class ComplexEvalScenario extends EvaluationScenarioBase {

    protected ComplexEvalScenario(TaskListBuilder taskListBuilder, ImageMetadataClient imageMetadataClient, SimulationServerConfig config, FogResourceService fogResourceService, AppRequestClient appRequestClient) {
        super(taskListBuilder, imageMetadataClient, config, fogResourceService, appRequestClient, 10);
    }

    @Override
    protected int[][] getRequestMatrix() {
        return new int[0][];
    }

    @Override
    protected void setupSimulation(TaskListBuilder.TaskListBuilderState.AppTaskBuilder simulationControlTrack, TaskListBuilder.TaskListBuilderState taskListBuilderState, BasicScenarioInfo basicScenarioInfo, List<TrackExecutionState> applications, boolean useAuction, ArrayList<TaskListBuilder.TaskListBuilderState.AppTaskBuilder> taskBuilders) {

        // upgrade apps
        // recover
        // fogs down
    }

    @Override
    public String getId() {
        return null;
    }
}
