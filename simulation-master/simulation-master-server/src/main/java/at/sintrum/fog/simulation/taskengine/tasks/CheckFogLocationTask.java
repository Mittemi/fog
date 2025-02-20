package at.sintrum.fog.simulation.taskengine.tasks;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.metadatamanager.api.ApplicationStateMetadataApi;
import at.sintrum.fog.metadatamanager.api.dto.ApplicationStateMetadata;
import at.sintrum.fog.simulation.taskengine.TrackExecutionState;

/**
 * Created by Michael Mittermayr on 24.08.2017.
 */
public class CheckFogLocationTask extends FogTaskBase {

    private final FogIdentification expectedLocation;
    private final ApplicationStateMetadataApi applicationStateMetadataApi;

    public CheckFogLocationTask(int offset, TrackExecutionState trackExecutionState, FogIdentification expectedLocation, ApplicationStateMetadataApi applicationStateMetadataApi) {
        super(offset, trackExecutionState, CheckFogLocationTask.class);
        this.expectedLocation = expectedLocation;
        this.applicationStateMetadataApi = applicationStateMetadataApi;
    }

    @Override
    protected boolean internalExecute() {

        ApplicationStateMetadata stateMetadata = applicationStateMetadataApi.getById(getTrackExecutionState().getInstanceId());
        if (stateMetadata == null || stateMetadata.getRunningAt() == null) return false;
        return stateMetadata.getRunningAt().isSameFog(expectedLocation) /*&& checkState(stateMetadata)*/;
    }

    private boolean checkState(ApplicationStateMetadata stateMetadata) {
        switch (stateMetadata.getState()) {
            case Running:
            case Standby:
            case Upgrade:
                return true;
            case Moving:
            case Retired:
                return false;
        }
        return false;
    }
}
