package at.sintrum.fog.simulation.taskengine.tasks;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.metadatamanager.api.ApplicationStateMetadataApi;

/**
 * Created by Michael Mittermayr on 24.08.2017.
 */
public class CheckFogLocationTask extends FogTaskBase {
    private final String instanceId;
    private final FogIdentification expectedLocation;
    private final ApplicationStateMetadataApi applicationStateMetadataApi;

    public CheckFogLocationTask(int offset, String instanceId, FogIdentification expectedLocation, ApplicationStateMetadataApi applicationStateMetadataApi) {
        super(offset, CheckFogLocationTask.class);
        this.instanceId = instanceId;
        this.expectedLocation = expectedLocation;
        this.applicationStateMetadataApi = applicationStateMetadataApi;
    }

    @Override
    protected boolean internalExecute() {

        FogIdentification applicationUrl = applicationStateMetadataApi.getApplicationUrl(instanceId);
        return applicationUrl.isSameFog(expectedLocation);
    }
}
