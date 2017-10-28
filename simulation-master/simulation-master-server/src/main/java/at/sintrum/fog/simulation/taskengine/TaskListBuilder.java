package at.sintrum.fog.simulation.taskengine;

import at.sintrum.fog.application.client.factory.ApplicationClientFactory;
import at.sintrum.fog.application.client.factory.TestApplicationClientFactory;
import at.sintrum.fog.applicationhousing.api.dto.AppIdentification;
import at.sintrum.fog.applicationhousing.client.api.AppEvolutionClient;
import at.sintrum.fog.applicationhousing.client.api.AppRecoveryClient;
import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.core.dto.ResourceInfo;
import at.sintrum.fog.deploymentmanager.client.factory.DeploymentManagerClientFactory;
import at.sintrum.fog.metadatamanager.api.ApplicationStateMetadataApi;
import at.sintrum.fog.metadatamanager.api.dto.DockerImageMetadata;
import at.sintrum.fog.metadatamanager.client.api.AppRequestClient;
import at.sintrum.fog.metadatamanager.client.api.ContainerMetadataClient;
import at.sintrum.fog.metadatamanager.client.api.ImageMetadataClient;
import at.sintrum.fog.metadatamanager.client.factory.MetadataManagerClientFactory;
import at.sintrum.fog.simulation.scenario.Scenario;
import at.sintrum.fog.simulation.service.FogCellStateService;
import at.sintrum.fog.simulation.service.FogResourceService;
import at.sintrum.fog.simulation.taskengine.tasks.*;
import org.joda.time.DateTime;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Michael Mittermayr on 01.09.2017.
 */
@Service
public class TaskListBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(TaskListAsyncInvoker.class);

    private final DeploymentManagerClientFactory deploymentManagerClientFactory;
    private final MetadataManagerClientFactory metadataManagerClientFactory;
    private final ApplicationStateMetadataApi applicationStateMetadataClient;
    private final ApplicationClientFactory applicationClientFactory;
    private final TestApplicationClientFactory testApplicationClientFactory;

    private final ContainerMetadataClient containerMetadataApi;
    private final ImageMetadataClient imageMetadataApi;
    private final AppEvolutionClient appEvolutionApi;
    private final AppRequestClient appRequestClient;
    private final FogResourceService fogResourceService;
    private final FogCellStateService fogCellStateService;
    private final AppRecoveryClient appRecovery;
    private final RedissonClient redissonClient;

    public TaskListBuilder(DeploymentManagerClientFactory deploymentManagerClientFactory,
                           MetadataManagerClientFactory metadataManagerClientFactory,
                           ApplicationStateMetadataApi applicationStateMetadataClient,
                           ApplicationClientFactory applicationClientFactory,
                           TestApplicationClientFactory testApplicationClientFactory,
                           ContainerMetadataClient containerMetadataApi,
                           ImageMetadataClient imageMetadataApi,
                           AppEvolutionClient appEvolutionApi,
                           AppRequestClient appRequestClient,
                           FogResourceService fogResourceService,
                           FogCellStateService fogCellStateService,
                           AppRecoveryClient appRecovery,
                           RedissonClient redissonClient) {
        this.deploymentManagerClientFactory = deploymentManagerClientFactory;
        this.metadataManagerClientFactory = metadataManagerClientFactory;
        this.applicationStateMetadataClient = applicationStateMetadataClient;
        this.applicationClientFactory = applicationClientFactory;
        this.testApplicationClientFactory = testApplicationClientFactory;
        this.containerMetadataApi = containerMetadataApi;
        this.imageMetadataApi = imageMetadataApi;


        this.appEvolutionApi = appEvolutionApi;
        this.appRequestClient = appRequestClient;
        this.fogResourceService = fogResourceService;
        this.fogCellStateService = fogCellStateService;
        this.appRecovery = appRecovery;
        this.redissonClient = redissonClient;
    }

    public class TaskListBuilderState {

        private final Map<Integer, Queue<FogTask>> tracks;
        private final Map<Integer, TrackExecutionState> state;
        private final Scenario scenario;
        private int currentId;

        private final ConcurrentHashMap<Integer, DateTime> simulationTime;

        private void updateSimulationTime(int id) {
            simulationTime.put(id, new DateTime());
        }

        public DateTime getSimulationTime(int id) {
            simulationTime.putIfAbsent(id, new DateTime());
            return simulationTime.get(id);
        }

        private TaskListBuilderState(Scenario scenario) {
            this.scenario = scenario;
            simulationTime = new ConcurrentHashMap<>();
            tracks = new ConcurrentHashMap<>();
            state = new ConcurrentHashMap<>();
        }

        public AppTaskBuilder createTrack() {
            tracks.putIfAbsent(currentId, new ConcurrentLinkedQueue<>());
            state.putIfAbsent(currentId, new TrackExecutionState(null));
            AppTaskBuilder appTaskBuilder = new AppTaskBuilder(tracks.get(currentId), getTrackState(currentId));
            currentId++;
            return appTaskBuilder;
        }

        public TaskListBuilderState resetMetadata() {
            ResetMetadataTask.reset(applicationStateMetadataClient, appEvolutionApi, appRecovery, fogResourceService, fogCellStateService, appRequestClient);
            return this;
        }

        public Scenario getScenario() {
            return scenario;
        }

        public TrackExecutionState getTrackState(int trackId) {
            return state.get(trackId);
        }

        public class AppTaskBuilder {

            private final DockerImageMetadata testAppMetadata;
            private final DockerImageMetadata anotherAppMetadata;
            private final TrackExecutionState trackExecutionState;

            private final Queue<FogTask> tasks;


            private AppTaskBuilder(Queue<FogTask> tasks, TrackExecutionState state) {
                this.tasks = tasks;
                trackExecutionState = state;
                List<DockerImageMetadata> all = metadataManagerClientFactory.createApplicationMetadataClient(null).getAll();

                testAppMetadata = all.stream().filter(x -> x.getApplicationName().equals("test-application") && x.isEnableDebugging() && x.getTag().equals("latest")).findFirst().orElse(null);
                anotherAppMetadata = all.stream().filter(x -> x.getApplicationName().equals("another-application") && x.isEnableDebugging() && x.getTag().equals("latest")).findFirst().orElse(null);

                if (testAppMetadata == null || anotherAppMetadata == null) {
                    LOG.error("Failed to find image metadata");
                }
            }

            private AppTaskBuilder addTask(FogTask task) {
                tasks.add(task);
                return this;
            }

            public AppTaskBuilder startTestApp(int offset, FogIdentification cloud) {
                return startApp(offset, cloud, testAppMetadata);
            }

            public AppTaskBuilder startAnotherApp(int offset, FogIdentification cloud) {
                return startApp(offset, cloud, anotherAppMetadata);
            }

            public AppTaskBuilder startApp(int offset, FogIdentification cloud, DockerImageMetadata metadata) {
                return addTask(new StartAppTask(0, trackExecutionState, deploymentManagerClientFactory, cloud, metadata.getId(), redissonClient, imageMetadataApi));
            }

            public AppTaskBuilder requestApp(int offset, FogIdentification target, int estimatedDuration) {
                return requestApp(offset, target, estimatedDuration, new AppRequestState(), 1);
            }

            public AppTaskBuilder requestApp(int offset, FogIdentification target, int estimatedDuration, int credits) {
                return requestApp(offset, target, estimatedDuration, new AppRequestState(), credits);
            }

            public AppTaskBuilder requestApp(int offset, FogIdentification target, int estimatedDuration, AppRequestState appRequestState, int credits) {
                return requestAppWithCredits(offset, target, estimatedDuration, appRequestState, credits);
            }

            public AppTaskBuilder requestAppWithCredits(int offset, FogIdentification target, int estimatedDuration, AppRequestState appRequestState, int credits) {
                return addTask(new RequestAppTask(offset, trackExecutionState, target, appRequestClient, estimatedDuration, appRequestState, credits));
            }

            public AppTaskBuilder checkLocation(int offset, FogIdentification expectedLocation) {
                return addTask(new CheckFogLocationTask(offset, trackExecutionState, expectedLocation, applicationStateMetadataClient));
            }

            public AppTaskBuilder finishWork(int offset) {
                return addTask(new FinishWorkTask(offset, trackExecutionState, testApplicationClientFactory, applicationStateMetadataClient));
            }

            public AppTaskBuilder removeApp(int offset) {
                return addTask(new RemoveAppTask(offset, trackExecutionState, applicationClientFactory, applicationStateMetadataClient, deploymentManagerClientFactory, containerMetadataApi));
            }

            public AppTaskBuilder logMessage(int offset, String message) {
                return addTask(new LogMessageTask(offset, message, trackExecutionState));
            }

            public AppTaskBuilder upgradeApp(int offset, DockerImageMetadata oldVersion, DockerImageMetadata newVersion) {
                return addTask(new AddUpgradeInfoTask(offset, trackExecutionState, appEvolutionApi, new AppIdentification(oldVersion.getId()), new AppIdentification(newVersion.getId())));
            }

            public AppTaskBuilder checkUpgraded(int offset, DockerImageMetadata newVersion) {
                return addTask(new CheckUpgradedTask(offset, trackExecutionState, appEvolutionApi, new AppIdentification(newVersion.getId()), containerMetadataApi, imageMetadataApi));
            }

            public AppTaskBuilder removeUpgradeInfo(int offset, DockerImageMetadata oldVersion) {
                return addTask(new RemoveUpgradeInfoTask(offset, trackExecutionState, appEvolutionApi, new AppIdentification(oldVersion.getId())));
            }

            public AppTaskBuilder resetMetadata(int offset) {
                return addTask(new ResetMetadataTask(offset, trackExecutionState, applicationStateMetadataClient, appEvolutionApi, appRecovery, fogResourceService, fogCellStateService, appRequestClient));
            }

            public AppTaskBuilder setResourceLimit(int offset, FogIdentification fogIdentification, ResourceInfo resourceInfo) {
                return addTask(new SetResourceLimitTask(offset, trackExecutionState, resourceInfo, fogIdentification, fogResourceService));
            }

            public AppTaskBuilder stopAppContainer(int offset, FogIdentification deploymentManagerLocation) {
                return addTask(new StopContainerTask(offset, trackExecutionState, deploymentManagerClientFactory, deploymentManagerLocation, containerMetadataApi));
            }

            public AppTaskBuilder startAppContainer(int offset, FogIdentification deploymentManagerLocation) {
                return addTask(new StartContainerTask(offset, trackExecutionState, deploymentManagerClientFactory, deploymentManagerLocation, containerMetadataApi));
            }

            public AppTaskBuilder startContainer(int offset, FogIdentification deploymentManagerLocation, TrackBuilderState state) {
                return addTask(new StartContainerTask(offset, trackExecutionState, deploymentManagerClientFactory, deploymentManagerLocation, containerMetadataApi, state));
            }

            public AppTaskBuilder checkReachability(int offset, boolean shouldBeReachable) {
                return addTask(new CheckAppReachabilityTask(offset, trackExecutionState, applicationClientFactory, applicationStateMetadataClient, shouldBeReachable));
            }

            public AppTaskBuilder setFogNetworkState(int offset, FogIdentification fogIdentification, boolean isOnline, boolean serviceOnly) {
                return addTask(new SetFogNetworkStateTask(offset, trackExecutionState, fogIdentification, isOnline, serviceOnly, fogCellStateService));
            }

            public AppTaskBuilder updateInstanceId(int offset) {
                return addTask(new UpdateInstanceIdTask(offset, trackExecutionState, appEvolutionApi));
            }

            public AppTaskBuilder updateScenarioState(int offset, TrackBuilderState trackBuilderState) {
                return addTask(new UpdateTrackBuilderStateTask(offset, trackExecutionState, trackBuilderState, containerMetadataApi));
            }
        }

        public List<Integer> getTrackIds() {
            return new LinkedList<>(tracks.keySet());
        }

        public Queue<FogTask> getTrack(int id) {
            return tracks.get(id);
        }

        public boolean isFinished(int trackId) {
            return getTrack(trackId).isEmpty();
        }

        public boolean isFinished() {
            return getTrackIds().stream().allMatch(this::isFinished);
        }

        public boolean runTaskIfPossible(int trackId) {
            Queue<FogTask> privateTaskList = getTrack(trackId);
            FogTask task;
            while ((task = privateTaskList.peek()) != null) {

                if (!task.shouldStart(getSimulationTime(trackId))) {
                    return true;
                }

                boolean result = task.execute();
                if (result || !task.repeatOnError()) {
                    getTrackState(trackId).taskFinished();
                    privateTaskList.poll();
                }

                if (!result) {
                    return false;
                } else {
                    updateSimulationTime(trackId);
                }
            }
            return true;
        }
    }

    public TaskListBuilderState newTaskList(Scenario scenario) {
        return new TaskListBuilderState(scenario);
    }
}
