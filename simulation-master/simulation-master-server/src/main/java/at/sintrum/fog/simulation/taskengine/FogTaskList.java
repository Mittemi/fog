package at.sintrum.fog.simulation.taskengine;

import at.sintrum.fog.application.client.api.TestApplicationClientFactory;
import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.deploymentmanager.api.dto.ApplicationStartRequest;
import at.sintrum.fog.deploymentmanager.client.factory.DeploymentManagerClientFactory;
import at.sintrum.fog.metadatamanager.api.ApplicationStateMetadataApi;
import at.sintrum.fog.metadatamanager.api.dto.DockerImageMetadata;
import at.sintrum.fog.metadatamanager.client.factory.MetadataManagerClientFactory;
import at.sintrum.fog.simulation.appclient.PlatformAppClientFactory;
import at.sintrum.fog.simulation.taskengine.tasks.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;

/**
 * Created by Michael Mittermayr on 24.08.2017.
 */
@Service
public class FogTaskList {

    private Map<Integer, Queue<FogTask>> taskList;
    private Map<Integer, DateTime> simulationTime;

    public DateTime getSimulationTime(int id) {
        simulationTime.putIfAbsent(id, new DateTime());
        return simulationTime.get(id);
    }

    private static final Logger LOG = LoggerFactory.getLogger(FogTaskList.class);

    private final DeploymentManagerClientFactory deploymentManagerClientFactory;
    private final MetadataManagerClientFactory metadataManagerClientFactory;
    private final ApplicationStateMetadataApi applicationStateMetadataClient;
    private final PlatformAppClientFactory platformAppClientFactory;
    private final TestApplicationClientFactory testApplicationClientFactory;

    public FogTaskList(DeploymentManagerClientFactory deploymentManagerClientFactory, MetadataManagerClientFactory metadataManagerClientFactory, ApplicationStateMetadataApi applicationStateMetadataClient, PlatformAppClientFactory platformAppClientFactory, TestApplicationClientFactory testApplicationClientFactory) {
        this.deploymentManagerClientFactory = deploymentManagerClientFactory;
        this.metadataManagerClientFactory = metadataManagerClientFactory;
        this.applicationStateMetadataClient = applicationStateMetadataClient;
        this.platformAppClientFactory = platformAppClientFactory;
        this.testApplicationClientFactory = testApplicationClientFactory;
    }


    public boolean build() {

        List<DockerImageMetadata> all = metadataManagerClientFactory.createApplicationMetadataClient(null).getAll();

        DockerImageMetadata testAppMetadata = all.stream().filter(x -> x.getApplicationName().equals("test-application") && x.isEnableDebugging()).findFirst().orElse(null);
        DockerImageMetadata anotherAppMetadata = all.stream().filter(x -> x.getApplicationName().equals("another-application") && x.isEnableDebugging()).findFirst().orElse(null);

        if (testAppMetadata == null || anotherAppMetadata == null) {
            LOG.error("Failed to find image metadata");
            return false;
        }

        taskList = new ConcurrentHashMap<>();

        String firstAppUUID = UUID.randomUUID().toString();
        String secondAppUUID = UUID.randomUUID().toString();

        FogIdentification cloud = FogIdentification.parseFogBaseUrl("192.168.1.10:8088");
        FogIdentification fogA = FogIdentification.parseFogBaseUrl("192.168.1.10:8080");

        addTask(0, new StartAppTask(0, deploymentManagerClientFactory, cloud, new ApplicationStartRequest(testAppMetadata.getId(), firstAppUUID)));
        addTask(0, new RequestAppTask(15, firstAppUUID, fogA, platformAppClientFactory, applicationStateMetadataClient));
        addTask(0, new CheckFogLocationTask(30, firstAppUUID, fogA, applicationStateMetadataClient));
        addTask(0, new FinishWorkTask(15, firstAppUUID, testApplicationClientFactory, applicationStateMetadataClient));
        addTask(0, new CheckFogLocationTask(30, firstAppUUID, cloud, applicationStateMetadataClient));
        addTask(0, new RequestAppTask(20, firstAppUUID, fogA, platformAppClientFactory, applicationStateMetadataClient));
        addTask(0, new NotifySimulationTrackFinishedTask(0, 0));

        addTask(1, new StartAppTask(0, deploymentManagerClientFactory, cloud, new ApplicationStartRequest(anotherAppMetadata.getId(), secondAppUUID)));
        addTask(0, new CheckFogLocationTask(30, firstAppUUID, fogA, applicationStateMetadataClient));
        addTask(1, new RequestAppTask(15, secondAppUUID, fogA, platformAppClientFactory, applicationStateMetadataClient));
        addTask(0, new CheckFogLocationTask(30, firstAppUUID, cloud, applicationStateMetadataClient));
        addTask(1, new FinishWorkTask(30, secondAppUUID, testApplicationClientFactory, applicationStateMetadataClient));
        addTask(1, new NotifySimulationTrackFinishedTask(0, 1));

        simulationTime = new ConcurrentHashMap<>();

        return true;
    }

    private void addTask(int id, FogTask task) {
        taskList.putIfAbsent(id, new ConcurrentLinkedQueue<>());
        taskList.get(id).add(task);
    }

    public Set<Integer> getIds() {
        return taskList.keySet();
    }

    public boolean isReady() {
        return simulationTime != null;
    }

    @Async
    public Future<Boolean> execute(int id) {
        Queue<FogTask> privateTaskList = taskList.get(id);
        FogTask task = null;
        while ((task = privateTaskList.peek()) != null) {

            if (!task.shouldStart(getSimulationTime(id))) {
                return new AsyncResult<>(true);
            }

            boolean result = task.execute();
            if (result || !task.repeatOnError()) {
                privateTaskList.poll();
            }

            if (!result) {
                return new AsyncResult<>(false);
            } else {
                updateSimulationTime(id);
            }
        }
        return new AsyncResult<>(true);
    }

    private void updateSimulationTime(int id) {
        simulationTime.put(id, new DateTime());
    }
}
