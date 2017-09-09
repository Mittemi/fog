package at.sintrum.fog.simulation.taskengine.tasks;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.deploymentmanager.client.factory.DeploymentManagerClientFactory;
import at.sintrum.fog.metadatamanager.api.ContainerMetadataApi;
import at.sintrum.fog.metadatamanager.api.dto.DockerContainerMetadata;
import at.sintrum.fog.simulation.taskengine.TrackExecutionState;

/**
 * Created by Michael Mittermayr on 04.09.2017.
 */
public class StopContainerTask extends FogTaskBase {

    private final DeploymentManagerClientFactory deploymentManagerClientFactory;
    private final FogIdentification deploymentManagerLocation;
    private final ContainerMetadataApi containerMetadataApi;

    public StopContainerTask(int offset, TrackExecutionState trackExecutionState, DeploymentManagerClientFactory deploymentManagerClientFactory, FogIdentification deploymentManagerLocation, ContainerMetadataApi containerMetadataApi) {
        super(offset, trackExecutionState, StopContainerTask.class);
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

        return deploymentManagerClientFactory.createContainerManagerClient(deploymentManagerLocation.toUrl()).stopContainer(containerMetadata.getContainerId());
    }
}
