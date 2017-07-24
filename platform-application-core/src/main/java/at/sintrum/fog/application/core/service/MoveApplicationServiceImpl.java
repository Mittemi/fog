package at.sintrum.fog.application.core.service;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.core.service.EnvironmentInfoService;
import at.sintrum.fog.deploymentmanager.api.dto.ApplicationMoveRequest;
import at.sintrum.fog.deploymentmanager.client.api.ApplicationManager;
import at.sintrum.fog.deploymentmanager.client.factory.DeploymentManagerClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Michael Mittermayr on 24.05.2017.
 */
//@Service
public class MoveApplicationServiceImpl implements MoveApplicationService {

    private final EnvironmentInfoService environmentInfoService;
    private final DeploymentManagerClientFactory deploymentManagerClientFactory;

    private final Logger LOGGER = LoggerFactory.getLogger(MoveApplicationServiceImpl.class);

    public MoveApplicationServiceImpl(EnvironmentInfoService environmentInfoService, DeploymentManagerClientFactory deploymentManagerClientFactory) {
        this.environmentInfoService = environmentInfoService;
        this.deploymentManagerClientFactory = deploymentManagerClientFactory;
    }

    @Override
    public void moveApplication(FogIdentification target) {
        String containerId = environmentInfoService.getOwnContainerId();
        String targetDeploymentMangerUrl = deploymentManagerClientFactory.buildUrl(target.getIp(), target.getPort());

        String sourceDeploymentManagerUrl = environmentInfoService.getFogBaseUrl();

        ApplicationManager applicationManagerClient = deploymentManagerClientFactory.createApplicationManagerClient(sourceDeploymentManagerUrl);
        ApplicationMoveRequest applicationMoveRequest = new ApplicationMoveRequest(containerId, targetDeploymentMangerUrl);
        applicationMoveRequest.setApplicationUrl(environmentInfoService.getOwnUrl());

        applicationManagerClient.moveApplication(applicationMoveRequest);
    }
}
