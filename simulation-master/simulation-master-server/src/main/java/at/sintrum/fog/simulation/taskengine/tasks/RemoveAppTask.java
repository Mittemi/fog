package at.sintrum.fog.simulation.taskengine.tasks;

import at.sintrum.fog.application.client.factory.ApplicationClientFactory;
import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.deploymentmanager.api.dto.ContainerInfo;
import at.sintrum.fog.deploymentmanager.client.factory.DeploymentManagerClientFactory;
import at.sintrum.fog.metadatamanager.api.ApplicationStateMetadataApi;
import at.sintrum.fog.metadatamanager.api.ContainerMetadataApi;
import at.sintrum.fog.metadatamanager.api.dto.DockerContainerMetadata;
import at.sintrum.fog.simulation.taskengine.TaskListBuilder;
import feign.RetryableException;

/**
 * Created by Michael Mittermayr on 28.08.2017.
 */
public class RemoveAppTask extends FogTaskBase {

    private final ApplicationClientFactory applicationClientFactory;
    private final ApplicationStateMetadataApi applicationStateMetadataApi;
    private final DeploymentManagerClientFactory deploymentManagerClientFactory;
    private final ContainerMetadataApi containerMetadataApi;

    public RemoveAppTask(int offset, TaskListBuilder.TaskListBuilderState.AppTaskBuilder.TrackExecutionState trackExecutionState, ApplicationClientFactory applicationClientFactory, ApplicationStateMetadataApi applicationStateMetadataApi, DeploymentManagerClientFactory deploymentManagerClientFactory, ContainerMetadataApi containerMetadataApi) {
        super(offset, trackExecutionState, RemoveAppTask.class);
        this.applicationClientFactory = applicationClientFactory;
        this.applicationStateMetadataApi = applicationStateMetadataApi;
        this.deploymentManagerClientFactory = deploymentManagerClientFactory;
        this.containerMetadataApi = containerMetadataApi;
    }

    @Override
    protected boolean internalExecute() {
        String instanceId = getTrackExecutionState().getInstanceId();
        FogIdentification applicationUrl = applicationStateMetadataApi.getApplicationUrl(instanceId);

        FogIdentification runningAt = applicationStateMetadataApi.getById(instanceId).getRunningAt();

        if (applicationUrl == null) return false;

        DockerContainerMetadata metadata = containerMetadataApi.getLatestByInstanceId(instanceId);
        ContainerInfo containerInfo = deploymentManagerClientFactory.createContainerManagerClient(runningAt.toUrl()).getContainerInfo(metadata.getContainerId());
        if (containerInfo != null) {
            try {
                applicationClientFactory.createAppLifecycleClient(applicationUrl.toUrl()).tearDownApplication();
            } catch (RetryableException e) {
                //this one is expected
            } catch (Exception ex) {
                getLogger().debug("TearDown call", ex);
            }
        }
        return true;
    }
}
