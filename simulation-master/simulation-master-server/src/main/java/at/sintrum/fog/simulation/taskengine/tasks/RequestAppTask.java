package at.sintrum.fog.simulation.taskengine.tasks;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.metadatamanager.client.api.AppRequestClient;
import at.sintrum.fog.simulation.taskengine.AppRequestState;
import at.sintrum.fog.simulation.taskengine.TrackExecutionState;

/**
 * Created by Michael Mittermayr on 24.08.2017.
 */
public class RequestAppTask extends FogTaskBase {

    private final FogIdentification targetLocation;
    private final AppRequestClient appRequestClient;
    private final int estimatedDuration;
    private final AppRequestState appRequestState;
    private final int credits;

    public RequestAppTask(int offset, TrackExecutionState trackExecutionState, FogIdentification targetLocation, AppRequestClient appRequestClient, int estimatedDuration, AppRequestState appRequestState, int credits) {
        super(offset, trackExecutionState, RequestAppTask.class);
        this.targetLocation = targetLocation;
        this.appRequestClient = appRequestClient;
        this.estimatedDuration = estimatedDuration;
        this.appRequestState = appRequestState;
        this.credits = credits;
    }

    @Override
    protected boolean internalExecute() {
        return requestApp(targetLocation, estimatedDuration, credits, appRequestClient, appRequestState, getTrackExecutionState());
    }
}
