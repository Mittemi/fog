package at.sintrum.fog.simulation.taskengine.tasks;

import at.sintrum.fog.metadatamanager.api.dto.DockerContainerMetadata;
import at.sintrum.fog.metadatamanager.client.api.ContainerMetadataClient;
import at.sintrum.fog.simulation.taskengine.TrackBuilderState;
import at.sintrum.fog.simulation.taskengine.TrackExecutionState;

/**
 * Created by Michael Mittermayr on 07.09.2017.
 */
public class UpdateTrackBuilderStateTask extends FogTaskBase {

    private final TrackBuilderState trackBuilderState;
    private final ContainerMetadataClient containerMetadataClient;

    public UpdateTrackBuilderStateTask(int offset, TrackExecutionState trackExecutionState, TrackBuilderState trackBuilderState, ContainerMetadataClient containerMetadataClient) {
        super(offset, trackExecutionState, UpdateTrackBuilderStateTask.class);
        this.trackBuilderState = trackBuilderState;
        this.containerMetadataClient = containerMetadataClient;
    }

    @Override
    protected boolean internalExecute() {
        trackBuilderState.setInstanceId(getTrackExecutionState().getInstanceId());

        DockerContainerMetadata containerMetadata = containerMetadataClient.getLatestByInstanceId(getTrackExecutionState().getInstanceId());

        if (containerMetadata != null) {
            trackBuilderState.setContainerId(containerMetadata.getContainerId());
            return true;
        }

        return false;
    }
}
