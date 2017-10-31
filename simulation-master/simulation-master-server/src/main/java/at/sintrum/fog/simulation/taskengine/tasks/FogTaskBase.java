package at.sintrum.fog.simulation.taskengine.tasks;

import at.sintrum.fog.applicationhousing.client.api.AppEvolutionClient;
import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.metadatamanager.api.dto.AppRequest;
import at.sintrum.fog.metadatamanager.api.dto.AppRequestDto;
import at.sintrum.fog.metadatamanager.api.dto.AppRequestResult;
import at.sintrum.fog.metadatamanager.api.dto.RequestState;
import at.sintrum.fog.metadatamanager.client.api.AppRequestClient;
import at.sintrum.fog.simulation.taskengine.AppRequestState;
import at.sintrum.fog.simulation.taskengine.TrackExecutionState;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * Created by Michael Mittermayr on 24.08.2017.
 */
public abstract class FogTaskBase implements FogTask {

    private final int offset;

    private final TrackExecutionState trackExecutionState;
    private final Logger LOG;

    public Logger getLogger() {
        return LOG;
    }

    protected FogTaskBase(int offset, TrackExecutionState trackExecutionState, Class<?> clazz) {

        this.offset = offset;
        this.trackExecutionState = trackExecutionState;

        LOG = LoggerFactory.getLogger(clazz);
    }

    @Override
    public boolean shouldStart(DateTime simulationStart) {
        return Seconds.secondsBetween(simulationStart, new DateTime()).isGreaterThan(Seconds.seconds(offset));
    }

    @Override
    public boolean execute() {
        try {
            return internalExecute();
        } catch (Exception ex) {
            LOG.error("Task failed", ex);
            return false;
        }
    }

    @Override
    public boolean repeatOnError() {
        return true;
    }

    protected abstract boolean internalExecute();

    public TrackExecutionState getTrackExecutionState() {
        return trackExecutionState;
    }

    public static boolean updateInstanceId(AppEvolutionClient appEvolutionClient, TrackExecutionState trackExecutionState) {
        String latestInstanceId = appEvolutionClient.getLatestInstanceId(trackExecutionState.getInstanceId());
        if (!StringUtils.isEmpty(latestInstanceId) && !trackExecutionState.getInstanceId().equals(latestInstanceId)) {
            trackExecutionState.setInstanceId(latestInstanceId);
            return true;
        }
        return false;
    }

    public static boolean requestApp(FogIdentification targetLocation, int estimatedDuration, int credits, AppRequestClient appRequestClient, AppRequestState appRequestState, TrackExecutionState trackExecutionState) {
        AppRequest appRequest = new AppRequest(targetLocation, trackExecutionState.getInstanceId(), estimatedDuration);
        AppRequestResult request = appRequestClient.request(new AppRequestDto(appRequest, credits, appRequestState.getRequestId()));
        if (request != null) {
            appRequestState.setRequestId(request.getInternalId());
            return true;
        }
        return false;
    }

    public static RequestState updateRequestState(AppRequestClient appRequestClient, AppRequestState appRequestState) {
        if (StringUtils.isEmpty(appRequestState.getRequestId())) {
            return null;
        }
        RequestState requestState = appRequestClient.getRequestState(appRequestState.getRequestId());
        appRequestState.setRequestState(requestState);
        return requestState;
    }
}
