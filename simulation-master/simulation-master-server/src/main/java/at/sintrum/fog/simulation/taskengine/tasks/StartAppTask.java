package at.sintrum.fog.simulation.taskengine.tasks;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.deploymentmanager.api.dto.ApplicationStartRequest;
import at.sintrum.fog.deploymentmanager.api.dto.FogOperationResult;
import at.sintrum.fog.deploymentmanager.client.api.ApplicationManager;
import at.sintrum.fog.deploymentmanager.client.factory.DeploymentManagerClientFactory;

/**
 * Created by Michael Mittermayr on 24.08.2017.
 */
public class StartAppTask extends FogTaskBase {

    private final DeploymentManagerClientFactory deploymentManagerClientFactory;
    private final FogIdentification target;
    private final ApplicationStartRequest applicationStartRequest;

    public StartAppTask(int offset, DeploymentManagerClientFactory deploymentManagerClientFactory, FogIdentification target, ApplicationStartRequest applicationStartRequest) {
        super(offset, StartAppTask.class);
        this.deploymentManagerClientFactory = deploymentManagerClientFactory;
        this.target = target;
        this.applicationStartRequest = applicationStartRequest;
    }

    @Override
    protected boolean internalExecute() {

        ApplicationManager applicationManagerClient = deploymentManagerClientFactory.createApplicationManagerClient(target.toUrl());
        FogOperationResult fogOperationResult = applicationManagerClient.requestApplicationStart(applicationStartRequest);

        return fogOperationResult.isSuccessful();
    }
}
