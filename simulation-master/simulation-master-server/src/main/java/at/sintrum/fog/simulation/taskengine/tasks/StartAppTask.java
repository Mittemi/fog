package at.sintrum.fog.simulation.taskengine.tasks;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.deploymentmanager.api.dto.ApplicationStartRequest;
import at.sintrum.fog.deploymentmanager.api.dto.FogOperationResult;
import at.sintrum.fog.deploymentmanager.client.api.ApplicationManagerClient;
import at.sintrum.fog.deploymentmanager.client.factory.DeploymentManagerClientFactory;
import at.sintrum.fog.metadatamanager.api.dto.DockerImageMetadata;
import at.sintrum.fog.metadatamanager.client.api.ImageMetadataClient;
import at.sintrum.fog.simulation.taskengine.TrackExecutionState;
import org.redisson.api.RedissonClient;

import java.util.UUID;

/**
 * Created by Michael Mittermayr on 24.08.2017.
 */
public class StartAppTask extends FogTaskBase {

    private final DeploymentManagerClientFactory deploymentManagerClientFactory;
    private final FogIdentification target;
    private final String imageMetadataId;
    private final RedissonClient redissonClient;
    private final ImageMetadataClient imageMetadataClient;

    public StartAppTask(int offset, TrackExecutionState trackExecutionState, DeploymentManagerClientFactory deploymentManagerClientFactory, FogIdentification target, String imageMetadataId, RedissonClient redissonClient, ImageMetadataClient imageMetadataClient) {
        super(offset, trackExecutionState, StartAppTask.class);
        this.deploymentManagerClientFactory = deploymentManagerClientFactory;
        this.target = target;
        this.imageMetadataId = imageMetadataId;
        this.redissonClient = redissonClient;
        this.imageMetadataClient = imageMetadataClient;
    }

    @Override
    protected boolean internalExecute() {

        DockerImageMetadata metadata = imageMetadataClient.getById(imageMetadataId);
        redissonClient.getQueue("App_Travel_" + metadata.getApplicationName()).clear();

        ApplicationManagerClient applicationManagerClient = deploymentManagerClientFactory.createApplicationManagerClient(target.toUrl());
        getTrackExecutionState().setInstanceId(UUID.randomUUID().toString());
        FogOperationResult fogOperationResult = applicationManagerClient.requestApplicationStart(new ApplicationStartRequest(imageMetadataId, getTrackExecutionState().getInstanceId()));

        return fogOperationResult.isSuccessful();
    }
}
