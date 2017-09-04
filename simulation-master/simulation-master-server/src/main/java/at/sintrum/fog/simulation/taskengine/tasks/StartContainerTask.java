package at.sintrum.fog.simulation.taskengine.tasks;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.deploymentmanager.client.factory.DeploymentManagerClientFactory;
import at.sintrum.fog.metadatamanager.api.ContainerMetadataApi;
import at.sintrum.fog.metadatamanager.api.dto.DockerContainerMetadata;
import at.sintrum.fog.simulation.taskengine.TaskListBuilder;

/**
 * Created by Michael Mittermayr on 04.09.2017.
 */
public class StartContainerTask extends FogTaskBase {

    private final DeploymentManagerClientFactory deploymentManagerClientFactory;
    private final FogIdentification deploymentManagerLocation;
    private final ContainerMetadataApi containerMetadataApi;

    public StartContainerTask(int offset, TaskListBuilder.TaskListBuilderState.AppTaskBuilder.TrackExecutionState trackExecutionState, DeploymentManagerClientFactory deploymentManagerClientFactory, FogIdentification deploymentManagerLocation, ContainerMetadataApi containerMetadataApi) {
        super(offset, trackExecutionState, StartContainerTask.class);
        this.deploymentManagerClientFactory = deploymentManagerClientFactory;
        this.deploymentManagerLocation = deploymentManagerLocation;
        this.containerMetadataApi = containerMetadataApi;
    }

    @Override
    protected boolean internalExecute() {

        DockerContainerMetadata containerMetadata = containerMetadataApi.getLatestByInstanceId(getTrackExecutionState().getInstanceId());

        if (containerMetadata == null) {
            return false;
        }
        if (!containerMetadata.getFogId().equals(deploymentManagerLocation.toFogId())) {
            return false;
        }

        return deploymentManagerClientFactory.createContainerManagerClient(deploymentManagerLocation.toUrl()).startContainer(containerMetadata.getContainerId());
    }
}
