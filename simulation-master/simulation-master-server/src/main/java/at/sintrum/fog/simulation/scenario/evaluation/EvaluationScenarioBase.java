package at.sintrum.fog.simulation.scenario.evaluation;

import at.sintrum.fog.core.dto.ResourceInfo;
import at.sintrum.fog.metadatamanager.api.dto.DockerImageMetadata;
import at.sintrum.fog.metadatamanager.client.api.AppRequestClient;
import at.sintrum.fog.metadatamanager.client.api.ImageMetadataClient;
import at.sintrum.fog.simulation.SimulationServerConfig;
import at.sintrum.fog.simulation.scenario.Scenario;
import at.sintrum.fog.simulation.scenario.dto.BasicScenarioInfo;
import at.sintrum.fog.simulation.service.FogResourceService;
import at.sintrum.fog.simulation.taskengine.TaskListBuilder;
import at.sintrum.fog.simulation.taskengine.TrackExecutionState;
import at.sintrum.fog.simulation.taskengine.tasks.WaitTillFinishedTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Michael Mittermayr on 31.10.2017.
 */
public abstract class EvaluationScenarioBase implements Scenario {

    protected final TaskListBuilder taskListBuilder;
    private final ImageMetadataClient imageMetadataClient;
    private final SimulationServerConfig config;
    private final FogResourceService fogResourceService;
    private final AppRequestClient appRequestClient;

    private static final Logger LOG = LoggerFactory.getLogger(EvaluationScenarioBase.class);
    private LinkedList<DockerImageMetadata> applications;

    protected EvaluationScenarioBase(TaskListBuilder taskListBuilder, ImageMetadataClient imageMetadataClient, SimulationServerConfig config, FogResourceService fogResourceService, AppRequestClient appRequestClient) {
        this.taskListBuilder = taskListBuilder;
        this.imageMetadataClient = imageMetadataClient;
        this.config = config;
        this.fogResourceService = fogResourceService;
        this.appRequestClient = appRequestClient;
    }

    @Override
    public TaskListBuilder.TaskListBuilderState build(BasicScenarioInfo basicScenarioInfo, boolean useAuction) {
        TaskListBuilder.TaskListBuilderState taskListBuilderState = taskListBuilder.newTaskList(this);
        LOG.debug("Reset metadata");
        taskListBuilderState.resetMetadata();

        if (useAuction) {
            appRequestClient.enableAuction();
        } else {
            appRequestClient.disableAuction();
        }

        if (StringUtils.isEmpty(config.getRegistryUrl())) {
            LOG.error("Simulation server config error: RegistryURL not set");
        }

        LOG.debug("Setup fog capacities");
        setFogCapacities(basicScenarioInfo);

        LOG.debug("Setup application images");
        setupImages(config.getRegistryUrl());

        LOG.debug("Setup tracks");
        setupTracks(taskListBuilderState, basicScenarioInfo, applications, useAuction);

        LOG.debug("Scenario setup completed");
        return taskListBuilderState;
    }

    protected void setFogCapacities(BasicScenarioInfo basicScenarioInfo) {
        fogResourceService.setResourceRestriction(basicScenarioInfo.getFogA(), ResourceInfo.fixedSized(1));
        fogResourceService.setResourceRestriction(basicScenarioInfo.getFogB(), ResourceInfo.fixedSized(2));
        fogResourceService.setResourceRestriction(basicScenarioInfo.getFogC(), ResourceInfo.fixedSized(3));
        fogResourceService.setResourceRestriction(basicScenarioInfo.getFogD(), ResourceInfo.fixedSized(4));
        fogResourceService.setResourceRestriction(basicScenarioInfo.getFogE(), ResourceInfo.fixedSized(5));
    }

    protected Map<String, Integer> getFogCredits(BasicScenarioInfo basicScenarioInfo) {
        Map<String, Integer> fogCredits = new ConcurrentHashMap<>();
        fogCredits.putIfAbsent(basicScenarioInfo.getFogA().toFogId(), basicScenarioInfo.getCreditsA());
        fogCredits.putIfAbsent(basicScenarioInfo.getFogB().toFogId(), basicScenarioInfo.getCreditsB());
        fogCredits.putIfAbsent(basicScenarioInfo.getFogC().toFogId(), basicScenarioInfo.getCreditsC());
        fogCredits.putIfAbsent(basicScenarioInfo.getFogD().toFogId(), basicScenarioInfo.getCreditsD());
        fogCredits.putIfAbsent(basicScenarioInfo.getFogE().toFogId(), basicScenarioInfo.getCreditsE());
        return fogCredits;
    }

    private void setupImages(String registry) {

        applications = new LinkedList<>();

        for (int i = 0; i < 10; i++) {
            applications.add(createImageMetadata(registry, "eval-app" + i, 12000 + i, true));
        }
    }


    private DockerImageMetadata createImageMetadata(String registy, String name, int port, boolean enableDebug) {

        Optional<DockerImageMetadata> first = imageMetadataClient.getAll().stream().filter(x -> x.isEnableDebugging() == enableDebug && x.getApplicationName().equals(name)).findFirst();

        if (first.isPresent()) {
            return first.get();
        }

        DockerImageMetadata imageMetadata = new DockerImageMetadata();
        imageMetadata.setImage(registy + ":5000/test-application");
        imageMetadata.setApplicationName(name);
        imageMetadata.setTag("latest");
        imageMetadata.setEurekaEnabled(true);
        imageMetadata.setEnableDebugging(enableDebug);
        imageMetadata.setEnvironment(Collections.singletonList("SERVER_PORT=" + port));
        imageMetadata.setPorts(Collections.singletonList(port));
        imageMetadata.setAppStorageDirectory("/app/storage/");

        return imageMetadataClient.store(imageMetadata);
    }

    private void setupTracks(TaskListBuilder.TaskListBuilderState taskListBuilderState, BasicScenarioInfo basicScenarioInfo, LinkedList<DockerImageMetadata> applications, boolean useAuction) {

        ArrayList<TaskListBuilder.TaskListBuilderState.AppTaskBuilder> taskBuilders = new ArrayList<>();

        WaitTillFinishedTask.State simulationState = new WaitTillFinishedTask.State();

        TaskListBuilder.TaskListBuilderState.AppTaskBuilder simulationControlTrack = taskListBuilderState.createTrack();
        simulationControlTrack
                .codedTask(120, () -> simulationState.getRunningApplications() == 10)
                .logMessage(0, "All apps running!");

        List<TrackExecutionState> applicationStates = new ArrayList<>();

        for (int i = 0; i < applications.size(); i++) {
            LOG.debug("Setup track: " + i);
            TaskListBuilder.TaskListBuilderState.AppTaskBuilder track = taskListBuilderState.createTrack();
            applicationStates.add(track.getTrackExecutionState());
            taskBuilders.add(track);
            track.logMessage(0, "Track for: " + applications.get(i).getApplicationName());

            track.startApp(i * 5, basicScenarioInfo.getCloud(), applications.get(i));

            track.checkReachability(10, true);
            track.codedTask(0, () -> {
                simulationState.appStarted();
                return true;
            });
            track.waitForRequestsToFinish(30, simulationState);

            track.logMessage(0, "Init cleanup tasks")
                    .ensureLocation(0, basicScenarioInfo.getCloud())
                    .logMessage(0, "App is back in cloud")
                    .removeApp(0)
                    .logMessage(0, "Track finished");
        }

        setupSimulation(simulationControlTrack, taskListBuilderState, basicScenarioInfo, applicationStates, useAuction, taskBuilders);

        simulationControlTrack
                .logMessage(0, "Simulation specific tasks finished, init cleanup. Apps can be still in the cloud!")
                .codedTask(0, () -> {
                    simulationState.setAllRequestsCompleted(true);
                    return true;
                });
    }

    protected abstract void setupSimulation(TaskListBuilder.TaskListBuilderState.AppTaskBuilder simulationControlTrack, TaskListBuilder.TaskListBuilderState taskListBuilderState, BasicScenarioInfo basicScenarioInfo, List<TrackExecutionState> applications, boolean useAuction, ArrayList<TaskListBuilder.TaskListBuilderState.AppTaskBuilder> taskBuilders);
}
