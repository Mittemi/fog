package at.sintrum.fog.simulation.taskengine.tasks;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.metadatamanager.api.dto.AppRequest;
import at.sintrum.fog.metadatamanager.api.dto.AppRequestResult;
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

    public RequestAppTask(int offset, TrackExecutionState trackExecutionState, FogIdentification targetLocation, AppRequestClient appRequestClient, int estimatedDuration, AppRequestState appRequestState) {
        super(offset, trackExecutionState, RequestAppTask.class);
        this.targetLocation = targetLocation;
        this.appRequestClient = appRequestClient;
        this.estimatedDuration = estimatedDuration;
        this.appRequestState = appRequestState;
    }

    @Override
    protected boolean internalExecute() {
        AppRequest appRequest = new AppRequest(targetLocation, getTrackExecutionState().getInstanceId(), estimatedDuration);
        AppRequestResult request = appRequestClient.request(appRequest);
        if (request != null) {
            appRequestState.setRequestId(request.getInternalId());
            return true;
        }
        return false;
    }
}
