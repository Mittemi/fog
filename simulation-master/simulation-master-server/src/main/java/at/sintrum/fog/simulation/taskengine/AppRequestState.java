package at.sintrum.fog.simulation.taskengine;

import at.sintrum.fog.metadatamanager.api.dto.RequestState;

import java.beans.Transient;

/**
 * Created by Michael Mittermayr on 28.10.2017.
 */
public class AppRequestState {

    private String requestId;

    private RequestState requestState;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public RequestState getRequestState() {
        return requestState;
    }

    public void setRequestState(RequestState requestState) {
        this.requestState = requestState;
    }

    @Transient
    public boolean isFinished() {
        return requestState != null && requestState.isFinished();
    }
}
