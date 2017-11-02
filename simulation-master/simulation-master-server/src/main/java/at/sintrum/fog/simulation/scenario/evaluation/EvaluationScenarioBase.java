package at.sintrum.fog.simulation.scenario.evaluation;

import at.sintrum.fog.core.dto.FogIdentification;
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

    private final int NUMBER_OF_APPS;

    private static final Logger LOG = LoggerFactory.getLogger(EvaluationScenarioBase.class);
    private LinkedList<DockerImageMetadata> applications;

    protected EvaluationScenarioBase(TaskListBuilder taskListBuilder, ImageMetadataClient imageMetadataClient, SimulationServerConfig config, FogResourceService fogResourceService, AppRequestClient appRequestClient, int numberOfApps) {
        this.taskListBuilder = taskListBuilder;
        this.imageMetadataClient = imageMetadataClient;
        this.config = config;
        this.fogResourceService = fogResourceService;
        this.appRequestClient = appRequestClient;
        NUMBER_OF_APPS = numberOfApps;
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
        setupImages();

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

    private void setupImages() {

        applications = new LinkedList<>();

        for (int i = 0; i < NUMBER_OF_APPS; i++) {
            applications.add(createImageMetadata("eval-app" + i, 12000 + i, true, false));
        }
    }


    protected DockerImageMetadata createImageMetadata(String name, int port, boolean enableDebug, boolean createNew) {

        if (!createNew) {
            Optional<DockerImageMetadata> first = imageMetadataClient.getAll()
                    .stream()
                    .filter(x -> x.isEnableDebugging() == enableDebug
                            && x.getApplicationName().equals(name)
                            && x.getTag().equals("latest"))
                    .findFirst();

            if (first.isPresent()) {
                return first.get();
            }
        }

        DockerImageMetadata imageMetadata = new DockerImageMetadata();
        imageMetadata.setImage(config.getRegistryUrl() + ":5000/test-application");
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
                .codedTask(120, () -> simulationState.getRunningApplications() == NUMBER_OF_APPS)
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

        List<FogRequestsManager.RequestInfo> requests = buildRequests(basicScenarioInfo);

        FogRequestsManager fogRequestManager = simulationControlTrack.createFogRequestManager(applicationStates, getFogCredits(basicScenarioInfo), basicScenarioInfo.getSecondsBetweenRequests(), requests);

        simulationControlTrack
                .logMessage(0, "Start fog request manager")
                .codedTask(0, () -> {
                    fogRequestManager.start();
                    return true;
                })
                .runFogRequestManager(fogRequestManager)
                .logMessage(0, "Fog request manager completed");

        setupSimulation(simulationControlTrack, taskListBuilderState, basicScenarioInfo, applicationStates, useAuction, taskBuilders);

        simulationControlTrack
                .logMessage(0, "Simulation specific tasks finished, init cleanup. Apps can be still in the cloud!")
                .codedTask(0, () -> {
                    simulationState.setAllRequestsCompleted(true);
                    return true;
                });
    }

    protected int[] getDuration() {
        return new int[]{1, 2, 2, 3, 3};
    }


    protected List<FogRequestsManager.RequestInfo> buildRequests(BasicScenarioInfo basicScenarioInfo) {
        List<FogRequestsManager.RequestInfo> requests = new LinkedList<>();

        int[][] requestMatrix = getRequestMatrix();

        FogIdentification[] fogs = new FogIdentification[]{
                basicScenarioInfo.getFogA(),
                basicScenarioInfo.getFogB(),
                basicScenarioInfo.getFogC(),
                basicScenarioInfo.getFogD(),
                basicScenarioInfo.getFogE()
        };

        int[] duration = getDuration();


        for (int i = 1; i < 20; i++) {
            for (int fogIdx = 0; fogIdx < 5; fogIdx++) {
                for (int appIdx = 0; appIdx < 10; appIdx++) {
                    int frequency = requestMatrix[fogIdx][appIdx];
                    if (frequency == 0) {
                        continue;
                    }
                    if (i % frequency == 0) {
                        requests.add(createRequest(appIdx, 60 * i, fogs[fogIdx], 60 * duration[fogIdx] - 30));
                    }
                }
            }
        }
        return requests;
    }

    protected FogRequestsManager.RequestInfo createRequest(int app, int offset, FogIdentification fog, int duration) {
        return new FogRequestsManager.RequestInfo(app, offset, fog, duration);
    }

    protected abstract int[][] getRequestMatrix();

    protected abstract void setupSimulation(TaskListBuilder.TaskListBuilderState.AppTaskBuilder simulationControlTrack, TaskListBuilder.TaskListBuilderState taskListBuilderState, BasicScenarioInfo basicScenarioInfo, List<TrackExecutionState> applications, boolean useAuction, ArrayList<TaskListBuilder.TaskListBuilderState.AppTaskBuilder> taskBuilders);
}
