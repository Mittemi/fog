package at.sintrum.fog.simulation.taskengine.tasks;

import at.sintrum.fog.applicationhousing.api.AppEvolutionApi;
import at.sintrum.fog.applicationhousing.api.dto.AppIdentification;
import at.sintrum.fog.metadatamanager.api.ContainerMetadataApi;
import at.sintrum.fog.metadatamanager.api.ImageMetadataApi;
import at.sintrum.fog.metadatamanager.api.dto.DockerContainerMetadata;
import at.sintrum.fog.metadatamanager.api.dto.DockerImageMetadata;
import at.sintrum.fog.simulation.taskengine.TrackExecutionState;

/**
 * Created by Michael Mittermayr on 03.09.2017.
 */
public class CheckUpgradedTask extends FogTaskBase {

    private final AppEvolutionApi appEvolutionApi;
    private final AppIdentification newVersion;
    private final ContainerMetadataApi containerMetadataApi;
    private final ImageMetadataApi imageMetadataApi;

    public CheckUpgradedTask(int offset, TrackExecutionState trackExecutionState, AppEvolutionApi appEvolutionApi, AppIdentification newVersion, ContainerMetadataApi containerMetadataApi, ImageMetadataApi imageMetadataApi) {
        super(offset, trackExecutionState, CheckUpgradedTask.class);
        this.appEvolutionApi = appEvolutionApi;
        this.newVersion = newVersion;
        this.containerMetadataApi = containerMetadataApi;
        this.imageMetadataApi = imageMetadataApi;
    }

    @Override
    protected boolean internalExecute() {

        String appInstanceId = getTrackExecutionState().getInstanceId();
        String latestInstanceId = appEvolutionApi.getLatestInstanceId(appInstanceId);

        // not yet upgraded
        if (appInstanceId.equals(latestInstanceId)) {
            return false;
        }

        DockerContainerMetadata containerMetadata = containerMetadataApi.getLatestByInstanceId(latestInstanceId);
        DockerImageMetadata imageMetadata = imageMetadataApi.getById(containerMetadata.getImageMetadataId());

        boolean result = imageMetadata.getId().equals(newVersion.getImageMetadataId()) || imageMetadata.getBaseImageId().equals(newVersion.getImageMetadataId());
        if (result) {
            getLogger().debug("App-InstanceID changed from '" + appInstanceId + "' to '" + latestInstanceId + "'");
            getTrackExecutionState().setInstanceId(latestInstanceId);
        }
        return result;
    }
}
