package at.sintrum.fog.simulation.taskengine.tasks;

import at.sintrum.fog.application.client.factory.TestApplicationClientFactory;
import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.metadatamanager.api.ApplicationStateMetadataApi;
import at.sintrum.fog.simulation.taskengine.TaskListBuilder;

/**
 * Created by Michael Mittermayr on 24.08.2017.
 */
public class FinishWorkTask extends FogTaskBase {

    private final TestApplicationClientFactory testApplicationClientFactory;
    private final ApplicationStateMetadataApi applicationStateMetadataApi;

    public FinishWorkTask(int offset, TaskListBuilder.TaskListBuilderState.AppTaskBuilder.TrackExecutionState trackExecutionState, TestApplicationClientFactory testApplicationClientFactory, ApplicationStateMetadataApi applicationStateMetadataApi) {
        super(offset, trackExecutionState, FinishWorkTask.class);
        this.testApplicationClientFactory = testApplicationClientFactory;
        this.applicationStateMetadataApi = applicationStateMetadataApi;
    }

    @Override
    protected boolean internalExecute() {
        FogIdentification applicationUrl = applicationStateMetadataApi.getApplicationUrl(getTrackExecutionState().getInstanceId());
        if (applicationUrl == null) return false;
        return testApplicationClientFactory.createWorkClient(applicationUrl.toUrl()).doSomeWork() != null;
    }
}
