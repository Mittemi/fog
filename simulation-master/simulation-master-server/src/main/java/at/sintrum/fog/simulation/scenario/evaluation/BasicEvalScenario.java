package at.sintrum.fog.simulation.scenario.evaluation;

import at.sintrum.fog.metadatamanager.api.dto.DockerImageMetadata;
import at.sintrum.fog.metadatamanager.client.api.AppRequestClient;
import at.sintrum.fog.metadatamanager.client.api.ImageMetadataClient;
import at.sintrum.fog.simulation.SimulationServerConfig;
import at.sintrum.fog.simulation.scenario.dto.BasicScenarioInfo;
import at.sintrum.fog.simulation.service.FogResourceService;
import at.sintrum.fog.simulation.taskengine.TaskListBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;

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
        super(taskListBuilder, imageMetadataClient, simulationServerConfig, fogResourceService, appRequestClient);
    }

    @Override
    protected void setupSimulation(TaskListBuilder.TaskListBuilderState.AppTaskBuilder simulationControlTrack, TaskListBuilder.TaskListBuilderState taskListBuilderState, BasicScenarioInfo basicScenarioInfo, LinkedList<DockerImageMetadata> applications, boolean useAuction, ArrayList<TaskListBuilder.TaskListBuilderState.AppTaskBuilder> taskBuilders) {




    }

    @Override
    public String getId() {
        return "basicEval";
    }
}
