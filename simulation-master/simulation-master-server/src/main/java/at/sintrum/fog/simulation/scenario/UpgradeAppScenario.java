package at.sintrum.fog.simulation.scenario;

import at.sintrum.fog.metadatamanager.api.ImageMetadataApi;
import at.sintrum.fog.metadatamanager.api.dto.DockerImageMetadata;
import at.sintrum.fog.simulation.scenario.dto.BasicScenarioInfo;
import at.sintrum.fog.simulation.taskengine.TaskListBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Michael Mittermayr on 02.09.2017.
 */
@Service
public class UpgradeAppScenario implements Scenario {

    private final TaskListBuilder taskListBuilder;
    private final ImageMetadataApi imageMetadataApi;

    public UpgradeAppScenario(TaskListBuilder taskListBuilder, ImageMetadataApi imageMetadataApi) {
        this.taskListBuilder = taskListBuilder;
        this.imageMetadataApi = imageMetadataApi;
    }

    @Override
    public String getId() {
        return "upgradeApp";
    }

    @Override
    public TaskListBuilder.TaskListBuilderState build(BasicScenarioInfo basicScenarioInfo, boolean useAuction) {

        List<DockerImageMetadata> all = imageMetadataApi.getAll();
        DockerImageMetadata debugVersion = all.stream().filter(x -> x.getApplicationName().equals("test-application") && x.getTag().equals("latest") && x.isEnableDebugging()).findFirst().orElse(null);
        DockerImageMetadata nonDebugVersion = all.stream().filter(x -> x.getApplicationName().equals("test-application") && x.getTag().equals("latest") && !x.isEnableDebugging()).findFirst().orElse(null);

        TaskListBuilder.TaskListBuilderState taskList = taskListBuilder.newTaskList(this, basicScenarioInfo);

        taskList.createTrack()
                .resetMetadata(0)
                .startApp(0, basicScenarioInfo.getCloud(), debugVersion)
                .checkLocation(10, basicScenarioInfo.getCloud())
                .logMessage(0, "App is running in the cloud now")
                .requestApp(0, basicScenarioInfo.getFogA(), 10, 10)
                .requestApp(0, basicScenarioInfo.getFogB(), 10, 7)
                .checkLocation(10, basicScenarioInfo.getFogA())
                .logMessage(0, "Now in Fog A")
                .upgradeApp(0, debugVersion, nonDebugVersion)
                .logMessage(0, "Upgrade Info added")
                .finishWork(0)
                .checkUpgraded(15, nonDebugVersion)
                .logMessage(0, "Upgrade completed")
                .checkLocation(0, basicScenarioInfo.getFogB())
                .finishWork(0)
                .checkLocation(0, basicScenarioInfo.getCloud())
                .removeApp(0)
                .removeUpgradeInfo(0, debugVersion)
                .logMessage(0, "Basic upgrade track completed");

        return taskList;
    }
}
