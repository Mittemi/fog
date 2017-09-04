package at.sintrum.fog.simulation.taskengine.tasks;

import at.sintrum.fog.application.client.factory.ApplicationClientFactory;
import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.metadatamanager.api.ApplicationStateMetadataApi;
import at.sintrum.fog.simulation.taskengine.TaskListBuilder;

/**
 * Created by Michael Mittermayr on 24.08.2017.
 */
public class RequestAppTask extends FogTaskBase {

    private final FogIdentification targetLocation;
    private final ApplicationClientFactory applicationClientFactory;
    private final ApplicationStateMetadataApi applicationStateMetadataApi;

    public RequestAppTask(int offset, TaskListBuilder.TaskListBuilderState.AppTaskBuilder.TrackExecutionState trackExecutionState, FogIdentification targetLocation, ApplicationClientFactory applicationClientFactory, ApplicationStateMetadataApi applicationStateMetadataApi) {
        super(offset, trackExecutionState, RequestAppTask.class);
        this.targetLocation = targetLocation;
        this.applicationClientFactory = applicationClientFactory;
        this.applicationStateMetadataApi = applicationStateMetadataApi;
    }

    @Override
    protected boolean internalExecute() {
        FogIdentification applicationUrl = applicationStateMetadataApi.getApplicationUrl(getTrackExecutionState().getInstanceId());
        if (applicationUrl == null) return false;
        return applicationClientFactory.createAppLifecycleClient(applicationUrl.toUrl()).requestApplication(targetLocation);
    }
}
