package at.sintrum.fog.simulation.scenario;

import at.sintrum.fog.metadatamanager.api.dto.DockerImageMetadata;
import at.sintrum.fog.metadatamanager.client.api.ImageMetadataClient;
import at.sintrum.fog.simulation.scenario.dto.BasicScenarioInfo;
import at.sintrum.fog.simulation.taskengine.TaskListBuilder;
import at.sintrum.fog.simulation.taskengine.TrackBuilderState;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Michael Mittermayr on 08.09.2017.
 */
@Service
public class ComplexScenario implements Scenario {

    private final TaskListBuilder taskListBuilder;
    private final ImageMetadataClient imageMetadataClient;

    public ComplexScenario(TaskListBuilder taskListBuilder, ImageMetadataClient imageMetadataClient) {
        this.taskListBuilder = taskListBuilder;
        this.imageMetadataClient = imageMetadataClient;
    }

    @Override
    public String getId() {
        return "complex";
    }

    @Override
    public TaskListBuilder.TaskListBuilderState build(BasicScenarioInfo basicScenarioInfo, boolean useAuction) {

        List<DockerImageMetadata> all = imageMetadataClient.getAll();
        DockerImageMetadata debugVersion = all.stream().filter(x -> x.getApplicationName().equals("test-application") && x.getTag().equals("latest") && x.isEnableDebugging()).findFirst().orElse(null);
        DockerImageMetadata nonDebugVersion = all.stream().filter(x -> x.getApplicationName().equals("test-application") && x.getTag().equals("latest") && !x.isEnableDebugging()).findFirst().orElse(null);

        TaskListBuilder.TaskListBuilderState taskListBuilderState = taskListBuilder.newTaskList(this);

        TrackBuilderState state = new TrackBuilderState();

        taskListBuilderState.createTrack()
                .resetMetadata(0)
                .startApp(0, basicScenarioInfo.getCloud(), debugVersion)
                .requestApp(0, basicScenarioInfo.getFogA(), 10)
                .requestApp(0, basicScenarioInfo.getFogB(), 10)
                .requestApp(0, basicScenarioInfo.getFogC(), 10)
                .logMessage(0, "App requested by all fogs")
                .checkLocation(0, basicScenarioInfo.getFogA())
                .finishWork(0)
                .logMessage(0, "Work in fogA finished")
                .checkLocation(0, basicScenarioInfo.getFogB())
                .upgradeApp(0, debugVersion, nonDebugVersion)
                .updateScenarioState(0, state)
                .setFogNetworkState(0, basicScenarioInfo.getFogB(), false, true)
                .stopAppContainer(0, basicScenarioInfo.getFogB())
                .updateInstanceId(10)       //wait for recovery
                .logMessage(0, "App should now be recovered and start original version")
                .setFogNetworkState(0, basicScenarioInfo.getFogB(), true, true)
                .startContainer(0, basicScenarioInfo.getFogB(), state)
                .logMessage(0, "Wait upgrade of recovered instance")
                .checkUpgraded(0, nonDebugVersion)
                .logMessage(0, "App was upgraded, continue in fogC")
                .checkLocation(0, basicScenarioInfo.getFogC())
                .logMessage(0, "All done! Tear down...")
                .removeApp(0);

        return taskListBuilderState;
    }
}
