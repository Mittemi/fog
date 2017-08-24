package at.sintrum.fog.simulation.taskengine.tasks;

import at.sintrum.fog.application.client.api.TestApplicationClientFactory;
import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.metadatamanager.api.ApplicationStateMetadataApi;

/**
 * Created by Michael Mittermayr on 24.08.2017.
 */
public class FinishWorkTask extends FogTaskBase {


    private final String instanceId;
    private final TestApplicationClientFactory testApplicationClientFactory;
    private final ApplicationStateMetadataApi applicationStateMetadataApi;

    public FinishWorkTask(int offset, String instanceId, TestApplicationClientFactory testApplicationClientFactory, ApplicationStateMetadataApi applicationStateMetadataApi) {
        super(offset, FinishWorkTask.class);
        this.instanceId = instanceId;
        this.testApplicationClientFactory = testApplicationClientFactory;
        this.applicationStateMetadataApi = applicationStateMetadataApi;
    }

    @Override
    protected boolean internalExecute() {
        FogIdentification applicationUrl = applicationStateMetadataApi.getApplicationUrl(instanceId);
        if (applicationUrl == null) return false;
        return testApplicationClientFactory.createWorkClient(applicationUrl.toUrl()).doSomeWork() != null;
    }
}
