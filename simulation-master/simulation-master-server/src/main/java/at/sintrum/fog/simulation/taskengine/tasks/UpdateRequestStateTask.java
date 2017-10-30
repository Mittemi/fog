package at.sintrum.fog.simulation.taskengine.tasks;

import at.sintrum.fog.metadatamanager.api.dto.RequestState;
import at.sintrum.fog.metadatamanager.client.api.AppRequestClient;
import at.sintrum.fog.simulation.taskengine.AppRequestState;
import at.sintrum.fog.simulation.taskengine.TrackExecutionState;

/**
 * Created by Michael Mittermayr on 30.10.2017.
 */
public class UpdateRequestStateTask extends FogTaskBase {
    private final AppRequestState appRequestState;
    private final AppRequestClient appRequestClient;
    private final boolean waitForFinished;

    public UpdateRequestStateTask(int offset, TrackExecutionState trackExecutionState, AppRequestState appRequestState, AppRequestClient appRequestClient, boolean waitForFinished) {
        super(offset, trackExecutionState, UpdateRequestStateTask.class);
        this.appRequestState = appRequestState;
        this.appRequestClient = appRequestClient;
        this.waitForFinished = waitForFinished;
    }

    @Override
    protected boolean internalExecute() {
        RequestState requestState = appRequestClient.getRequestState(appRequestState.getRequestId());
        appRequestState.setRequestState(requestState);
        return !waitForFinished || requestState.isFinished();
    }
}
