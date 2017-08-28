package at.sintrum.fog.simulation.taskengine.tasks;

import at.sintrum.fog.application.client.ApplicationClientFactory;
import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.metadatamanager.api.ApplicationStateMetadataApi;

/**
 * Created by Michael Mittermayr on 24.08.2017.
 */
public class RequestAppTask extends FogTaskBase {

    private final String instanceId;
    private final FogIdentification targetLocation;
    private final ApplicationClientFactory applicationClientFactory;
    private final ApplicationStateMetadataApi applicationStateMetadataApi;

    public RequestAppTask(int offset, String instanceId, FogIdentification targetLocation, ApplicationClientFactory applicationClientFactory, ApplicationStateMetadataApi applicationStateMetadataApi) {
        super(offset, RequestAppTask.class);
        this.instanceId = instanceId;
        this.targetLocation = targetLocation;
        this.applicationClientFactory = applicationClientFactory;
        this.applicationStateMetadataApi = applicationStateMetadataApi;
    }

    @Override
    protected boolean internalExecute() {
        FogIdentification applicationUrl = applicationStateMetadataApi.getApplicationUrl(instanceId);
        if (applicationUrl == null) return false;
        return applicationClientFactory.createAppLifecycleClient(applicationUrl.toUrl()).requestApplication(targetLocation);
    }
}
