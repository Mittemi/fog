package at.sintrum.fog.simulation.taskengine;

import at.sintrum.fog.application.client.ApplicationClientFactory;
import at.sintrum.fog.application.client.api.TestApplicationClientFactory;
import at.sintrum.fog.applicationhousing.api.AppEvolutionApi;
import at.sintrum.fog.applicationhousing.api.dto.AppIdentification;
import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.core.dto.ResourceInfo;
import at.sintrum.fog.deploymentmanager.client.factory.DeploymentManagerClientFactory;
import at.sintrum.fog.metadatamanager.api.ApplicationStateMetadataApi;
import at.sintrum.fog.metadatamanager.api.ContainerMetadataApi;
import at.sintrum.fog.metadatamanager.api.ImageMetadataApi;
import at.sintrum.fog.metadatamanager.api.dto.DockerImageMetadata;
import at.sintrum.fog.metadatamanager.client.factory.MetadataManagerClientFactory;
import at.sintrum.fog.simulation.service.FogResourceService;
import at.sintrum.fog.simulation.taskengine.tasks.*;
import org.joda.time.DateTime;
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

    private final ContainerMetadataApi containerMetadataApi;
    private final ImageMetadataApi imageMetadataApi;
    private final AppEvolutionApi appEvolutionApi;
    private final FogResourceService fogResourceService;

    public TaskListBuilder(DeploymentManagerClientFactory deploymentManagerClientFactory,
                           MetadataManagerClientFactory metadataManagerClientFactory,
                           ApplicationStateMetadataApi applicationStateMetadataClient,
                           ApplicationClientFactory applicationClientFactory,
                           TestApplicationClientFactory testApplicationClientFactory,
                           ContainerMetadataApi containerMetadataApi,
                           ImageMetadataApi imageMetadataApi,
                           AppEvolutionApi appEvolutionApi,
                           FogResourceService fogResourceService) {
        this.deploymentManagerClientFactory = deploymentManagerClientFactory;
        this.metadataManagerClientFactory = metadataManagerClientFactory;
        this.applicationStateMetadataClient = applicationStateMetadataClient;
        this.applicationClientFactory = applicationClientFactory;
        this.testApplicationClientFactory = testApplicationClientFactory;
        this.containerMetadataApi = containerMetadataApi;
        this.imageMetadataApi = imageMetadataApi;


        this.appEvolutionApi = appEvolutionApi;
        this.fogResourceService = fogResourceService;
    }

    public class TaskListBuilderState {

        private final Map<Integer, Queue<FogTask>> tracks;
        private int currentId;

        private final ConcurrentHashMap<Integer, DateTime> simulationTime;

        private void updateSimulationTime(int id) {
            simulationTime.put(id, new DateTime());
        }

        public DateTime getSimulationTime(int id) {
            simulationTime.putIfAbsent(id, new DateTime());
            return simulationTime.get(id);
        }

        private TaskListBuilderState() {
            simulationTime = new ConcurrentHashMap<>();
            tracks = new ConcurrentHashMap<>();
        }

        public AppTaskBuilder createTrack() {
            tracks.putIfAbsent(currentId, new ConcurrentLinkedQueue<>());
            AppTaskBuilder appTaskBuilder = new AppTaskBuilder(tracks.get(currentId));
            currentId++;
            return appTaskBuilder;
        }

        public TaskListBuilderState resetMetadata() {
            ResetMetadataTask.reset(applicationStateMetadataClient, appEvolutionApi, fogResourceService);
            return this;
        }

        public class AppTaskBuilder {

            private final DockerImageMetadata testAppMetadata;
            private final DockerImageMetadata anotherAppMetadata;

            private final TrackExecutionState trackExecutionState;

            public class TrackExecutionState {
                private String instanceId;

                public TrackExecutionState(String instanceId) {
                    this.instanceId = instanceId;
                }

                public String getInstanceId() {
                    return instanceId;
                }

                public void setInstanceId(String instanceId) {
                    this.instanceId = instanceId;
                }
            }

            private final Queue<FogTask> tasks;


            private AppTaskBuilder(Queue<FogTask> tasks) {
                this.tasks = tasks;
                trackExecutionState = new TrackExecutionState(null);
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
                return addTask(new StartAppTask(0, trackExecutionState, deploymentManagerClientFactory, cloud, metadata.getId()));
            }

            public AppTaskBuilder requestApp(int offset, FogIdentification target) {
                return addTask(new RequestAppTask(offset, trackExecutionState, target, applicationClientFactory, applicationStateMetadataClient));
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
                return addTask(new ResetMetadataTask(offset, trackExecutionState, applicationStateMetadataClient, appEvolutionApi, fogResourceService));
            }

            public AppTaskBuilder setResourceLimit(int offset, FogIdentification fogIdentification, ResourceInfo resourceInfo) {
                return addTask(new SetResourceLimitTask(offset, trackExecutionState, resourceInfo, fogIdentification, fogResourceService));
            }
        }

        public List<Integer> getTrackIds() {
            return new LinkedList<>(tracks.keySet());
        }

        public Queue<FogTask> getTrack(int id) {
            return tracks.get(id);
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

    public TaskListBuilderState newTaskList() {
        return new TaskListBuilderState();
    }
}
