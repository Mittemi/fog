package at.sintrum.fog.simulation.taskengine;

import at.sintrum.fog.application.client.ApplicationClientFactory;
import at.sintrum.fog.application.client.api.TestApplicationClientFactory;
import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.deploymentmanager.api.dto.ApplicationStartRequest;
import at.sintrum.fog.deploymentmanager.client.factory.DeploymentManagerClientFactory;
import at.sintrum.fog.metadatamanager.api.ApplicationStateMetadataApi;
import at.sintrum.fog.metadatamanager.api.ContainerMetadataApi;
import at.sintrum.fog.metadatamanager.api.dto.DockerImageMetadata;
import at.sintrum.fog.metadatamanager.client.factory.MetadataManagerClientFactory;
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
    private final ApplicationClientFactory applicationClientFactory;
    private final TestApplicationClientFactory testApplicationClientFactory;

    private final ContainerMetadataApi containerMetadataApi;

    public FogTaskList(ContainerMetadataApi containerMetadataApi, DeploymentManagerClientFactory deploymentManagerClientFactory, MetadataManagerClientFactory metadataManagerClientFactory, ApplicationStateMetadataApi applicationStateMetadataClient, ApplicationClientFactory applicationClientFactory, TestApplicationClientFactory testApplicationClientFactory) {
        this.containerMetadataApi = containerMetadataApi;
        this.deploymentManagerClientFactory = deploymentManagerClientFactory;
        this.metadataManagerClientFactory = metadataManagerClientFactory;
        this.applicationStateMetadataClient = applicationStateMetadataClient;
        this.applicationClientFactory = applicationClientFactory;
        this.testApplicationClientFactory = testApplicationClientFactory;
    }


    public boolean build() {

        List<DockerImageMetadata> all = metadataManagerClientFactory.createApplicationMetadataClient(null).getAll();

        DockerImageMetadata testAppMetadata = all.stream().filter(x -> x.getApplicationName().equals("test-application") && x.isEnableDebugging() && x.getTag().equals("latest")).findFirst().orElse(null);
        DockerImageMetadata anotherAppMetadata = all.stream().filter(x -> x.getApplicationName().equals("another-application") && x.isEnableDebugging() && x.getTag().equals("latest")).findFirst().orElse(null);

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
        addTask(0, new RequestAppTask(15, firstAppUUID, fogA, applicationClientFactory, applicationStateMetadataClient));
        addTask(0, new CheckFogLocationTask(30, firstAppUUID, fogA, applicationStateMetadataClient));
        addTask(0, new FinishWorkTask(15, firstAppUUID, testApplicationClientFactory, applicationStateMetadataClient));

        addTask(0, new CheckFogLocationTask(30, firstAppUUID, cloud, applicationStateMetadataClient));
        addTask(0, new RequestAppTask(20, firstAppUUID, fogA, applicationClientFactory, applicationStateMetadataClient));


        //TODO: task timeout
        addTask(1, new StartAppTask(0, deploymentManagerClientFactory, cloud, new ApplicationStartRequest(anotherAppMetadata.getId(), secondAppUUID)));
        addTask(1, new CheckFogLocationTask(30, secondAppUUID, cloud, applicationStateMetadataClient));
        addTask(1, new RequestAppTask(15, secondAppUUID, fogA, applicationClientFactory, applicationStateMetadataClient));

        addTask(1, new CheckFogLocationTask(30, secondAppUUID, fogA, applicationStateMetadataClient));
        addTask(1, new FinishWorkTask(30, secondAppUUID, testApplicationClientFactory, applicationStateMetadataClient));


        // CLEANUP
        addTask(0, new CheckFogLocationTask(30, firstAppUUID, fogA, applicationStateMetadataClient));
        addTask(0, new RemoveAppTask(0, firstAppUUID, applicationClientFactory, applicationStateMetadataClient, deploymentManagerClientFactory, containerMetadataApi));
        addTask(0, new NotifySimulationTrackFinishedTask(0, 0));


        addTask(1, new CheckFogLocationTask(30, secondAppUUID, cloud, applicationStateMetadataClient));
        addTask(1, new RemoveAppTask(0, secondAppUUID, applicationClientFactory, applicationStateMetadataClient, deploymentManagerClientFactory, containerMetadataApi));
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
