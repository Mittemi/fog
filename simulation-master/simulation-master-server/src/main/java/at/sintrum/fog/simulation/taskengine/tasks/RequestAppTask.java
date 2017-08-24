package at.sintrum.fog.simulation.taskengine.tasks;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.metadatamanager.api.ApplicationStateMetadataApi;
import at.sintrum.fog.simulation.appclient.PlatformAppClientFactory;

/**
 * Created by Michael Mittermayr on 24.08.2017.
 */
public class RequestAppTask extends FogTaskBase {

    private final String instanceId;
    private final FogIdentification targetLocation;
    private final PlatformAppClientFactory platformAppClientFactory;
    private final ApplicationStateMetadataApi applicationStateMetadataApi;

    public RequestAppTask(int offset, String instanceId, FogIdentification targetLocation, PlatformAppClientFactory platformAppClientFactory, ApplicationStateMetadataApi applicationStateMetadataApi) {
        super(offset, RequestAppTask.class);
        this.instanceId = instanceId;
        this.targetLocation = targetLocation;
        this.platformAppClientFactory = platformAppClientFactory;
        this.applicationStateMetadataApi = applicationStateMetadataApi;
    }

    @Override
    protected boolean internalExecute() {
        FogIdentification applicationUrl = applicationStateMetadataApi.getApplicationUrl(instanceId);
        return platformAppClientFactory.createPlatformAppClient(applicationUrl.toUrl()).requestApplication(targetLocation);
    }
}
