package at.sintrum.fog.simulation.taskengine.tasks;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.deploymentmanager.api.dto.ApplicationStartRequest;
import at.sintrum.fog.deploymentmanager.api.dto.FogOperationResult;
import at.sintrum.fog.deploymentmanager.client.api.ApplicationManagerClient;
import at.sintrum.fog.deploymentmanager.client.factory.DeploymentManagerClientFactory;
import at.sintrum.fog.simulation.taskengine.TaskListBuilder;

import java.util.UUID;

/**
 * Created by Michael Mittermayr on 24.08.2017.
 */
public class StartAppTask extends FogTaskBase {

    private final DeploymentManagerClientFactory deploymentManagerClientFactory;
    private final FogIdentification target;
    private final String imageMetadataId;

    public StartAppTask(int offset, TaskListBuilder.TaskListBuilderState.AppTaskBuilder.TrackExecutionState trackExecutionState, DeploymentManagerClientFactory deploymentManagerClientFactory, FogIdentification target, String imageMetadataId) {
        super(offset, trackExecutionState, StartAppTask.class);
        this.deploymentManagerClientFactory = deploymentManagerClientFactory;
        this.target = target;
        this.imageMetadataId = imageMetadataId;
    }

    @Override
    protected boolean internalExecute() {

        ApplicationManagerClient applicationManagerClient = deploymentManagerClientFactory.createApplicationManagerClient(target.toUrl());
        getTrackExecutionState().setInstanceId(UUID.randomUUID().toString());
        FogOperationResult fogOperationResult = applicationManagerClient.requestApplicationStart(new ApplicationStartRequest(imageMetadataId, getTrackExecutionState().getInstanceId()));

        return fogOperationResult.isSuccessful();
    }
}
