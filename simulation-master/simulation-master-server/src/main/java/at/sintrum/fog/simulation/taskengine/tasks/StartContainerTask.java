package at.sintrum.fog.simulation.taskengine.tasks;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.deploymentmanager.client.factory.DeploymentManagerClientFactory;
import at.sintrum.fog.metadatamanager.api.ContainerMetadataApi;
import at.sintrum.fog.metadatamanager.api.dto.DockerContainerMetadata;
import at.sintrum.fog.simulation.taskengine.TrackBuilderState;
import at.sintrum.fog.simulation.taskengine.TrackExecutionState;
import org.springframework.util.StringUtils;

/**
 * Created by Michael Mittermayr on 04.09.2017.
 */
public class StartContainerTask extends FogTaskBase {

    private final DeploymentManagerClientFactory deploymentManagerClientFactory;
    private final FogIdentification deploymentManagerLocation;
    private final ContainerMetadataApi containerMetadataApi;
    private final TrackBuilderState trackBuilderState;

    public StartContainerTask(int offset, TrackExecutionState trackExecutionState, DeploymentManagerClientFactory deploymentManagerClientFactory, FogIdentification deploymentManagerLocation, ContainerMetadataApi containerMetadataApi) {
        this(offset, trackExecutionState, deploymentManagerClientFactory, deploymentManagerLocation, containerMetadataApi, null);
    }

    public StartContainerTask(int offset, TrackExecutionState trackExecutionState, DeploymentManagerClientFactory deploymentManagerClientFactory, FogIdentification deploymentManagerLocation, ContainerMetadataApi containerMetadataApi, TrackBuilderState trackBuilderState) {
        super(offset, trackExecutionState, StartContainerTask.class);
        this.deploymentManagerClientFactory = deploymentManagerClientFactory;
        this.deploymentManagerLocation = deploymentManagerLocation;
        this.containerMetadataApi = containerMetadataApi;
        this.trackBuilderState = trackBuilderState;
    }

    @Override
    protected boolean internalExecute() {

        String containerId = trackBuilderState != null ? trackBuilderState.getContainerId() : null;

        if (StringUtils.isEmpty(containerId)) {
            DockerContainerMetadata containerMetadata = containerMetadataApi.getLatestByInstanceId(getTrackExecutionState().getInstanceId());

            if (containerMetadata == null) {
                return false;
            }
            if (!containerMetadata.getFogId().equals(deploymentManagerLocation.toFogId())) {
                return false;
            }
            containerId = containerMetadata.getContainerId();
        }
        return deploymentManagerClientFactory.createContainerManagerClient(deploymentManagerLocation.toUrl()).startContainer(containerId);
    }
}
