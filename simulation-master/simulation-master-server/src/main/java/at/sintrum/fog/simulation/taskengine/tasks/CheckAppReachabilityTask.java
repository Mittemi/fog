package at.sintrum.fog.simulation.taskengine.tasks;

import at.sintrum.fog.application.client.ApplicationClientFactory;
import at.sintrum.fog.application.core.api.dto.AppInfo;
import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.metadatamanager.api.ApplicationStateMetadataApi;
import at.sintrum.fog.simulation.taskengine.TaskListBuilder;

/**
 * Created by Michael Mittermayr on 04.09.2017.
 */
public class CheckAppReachabilityTask extends FogTaskBase {

    private final ApplicationClientFactory applicationClientFactory;
    private final ApplicationStateMetadataApi applicationStateMetadataApi;
    private final boolean shouldBeReachable;

    public CheckAppReachabilityTask(int offset, TaskListBuilder.TaskListBuilderState.AppTaskBuilder.TrackExecutionState trackExecutionState, ApplicationClientFactory applicationClientFactory, ApplicationStateMetadataApi applicationStateMetadataApi, boolean shouldBeReachable) {
        super(offset, trackExecutionState, CheckAppReachabilityTask.class);
        this.applicationClientFactory = applicationClientFactory;
        this.applicationStateMetadataApi = applicationStateMetadataApi;
        this.shouldBeReachable = shouldBeReachable;
    }

    @Override
    protected boolean internalExecute() {

        FogIdentification applicationUrl = applicationStateMetadataApi.getApplicationUrl(getTrackExecutionState().getInstanceId());

        if (applicationUrl == null)
            return false;

        try {
            AppInfo info = applicationClientFactory.createApplicationInfoClient(applicationUrl.toUrl()).info();

            if (shouldBeReachable)
                return info != null;
            return false;
        } catch (Exception ex) {
            return !shouldBeReachable;
        }
    }
}
