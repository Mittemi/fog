package at.sintrum.fog.simulation.scenario.evaluation;

import at.sintrum.fog.metadatamanager.client.api.AppRequestClient;
import at.sintrum.fog.metadatamanager.client.api.ImageMetadataClient;
import at.sintrum.fog.simulation.SimulationServerConfig;
import at.sintrum.fog.simulation.scenario.dto.BasicScenarioInfo;
import at.sintrum.fog.simulation.service.FogResourceService;
import at.sintrum.fog.simulation.taskengine.TaskListBuilder;
import at.sintrum.fog.simulation.taskengine.TrackExecutionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael Mittermayr on 31.10.2017.
 */
@Service
public class BasicEvalScenario extends EvaluationScenarioBase {

    private Logger LOG = LoggerFactory.getLogger(BasicEvalScenario.class);

    public BasicEvalScenario(TaskListBuilder taskListBuilder,
                             ImageMetadataClient imageMetadataClient,
                             SimulationServerConfig simulationServerConfig,
                             FogResourceService fogResourceService,
                             AppRequestClient appRequestClient) {
        super(taskListBuilder, imageMetadataClient, simulationServerConfig, fogResourceService, appRequestClient, 10);
    }

    @Override
    protected void setupSimulation(TaskListBuilder.TaskListBuilderState.AppTaskBuilder simulationControlTrack, TaskListBuilder.TaskListBuilderState taskListBuilderState, BasicScenarioInfo basicScenarioInfo, List<TrackExecutionState> applications, boolean useAuction, ArrayList<TaskListBuilder.TaskListBuilderState.AppTaskBuilder> taskBuilders) {

    }


    protected int[][] getRequestMatrix() {
        return BasicEvalScenario.getBasicEvalRequestMatrix();
    }

    public static int[][] getBasicEvalRequestMatrix() {
        int[][] requestMatrix = new int[5][10];
        //FOG 0
        requestMatrix[0][0] = 0;
        requestMatrix[0][1] = 5;
        requestMatrix[0][2] = 10;
        requestMatrix[0][3] = 0;
        requestMatrix[0][4] = 0;
        requestMatrix[0][5] = 0;
        requestMatrix[0][6] = 20;
        requestMatrix[0][7] = 5;
        requestMatrix[0][8] = 0;
        requestMatrix[0][9] = 30;
        //FOG 1
        requestMatrix[1][0] = 20;
        requestMatrix[1][1] = 30;
        requestMatrix[1][2] = 10;
        requestMatrix[1][3] = 8;
        requestMatrix[1][4] = 0;
        requestMatrix[1][5] = 10;
        requestMatrix[1][6] = 0;
        requestMatrix[1][7] = 0;
        requestMatrix[1][8] = 8;
        requestMatrix[1][9] = 0;
        //FOG 2
        requestMatrix[2][0] = 0;
        requestMatrix[2][1] = 5;
        requestMatrix[2][2] = 0;
        requestMatrix[2][3] = 0;
        requestMatrix[2][4] = 8;
        requestMatrix[2][5] = 0;
        requestMatrix[2][6] = 20;
        requestMatrix[2][7] = 0;
        requestMatrix[2][8] = 0;
        requestMatrix[2][9] = 7;
        //FOG 3
        requestMatrix[3][0] = 15;
        requestMatrix[3][1] = 30;
        requestMatrix[3][2] = 10;
        requestMatrix[3][3] = 0;
        requestMatrix[3][4] = 0;
        requestMatrix[3][5] = 5;
        requestMatrix[3][6] = 10;
        requestMatrix[3][7] = 15;
        requestMatrix[3][8] = 0;
        requestMatrix[3][9] = 9;
        //FOG 4
        requestMatrix[4][0] = 5;
        requestMatrix[4][1] = 0;
        requestMatrix[4][2] = 0;
        requestMatrix[4][3] = 5;
        requestMatrix[4][4] = 5;
        requestMatrix[4][5] = 0;
        requestMatrix[4][6] = 0;
        requestMatrix[4][7] = 0;
        requestMatrix[4][8] = 5;
        requestMatrix[4][9] = 15;
        return requestMatrix;
    }

    @Override
    public String getId() {
        return "basicEval";
    }
}
