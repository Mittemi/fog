package at.sintrum.fog.application.core.service;

import at.sintrum.fog.core.service.EnvironmentInfoService;
import at.sintrum.fog.deploymentmanager.api.dto.ApplicationMoveRequest;
import at.sintrum.fog.deploymentmanager.client.api.ApplicationManager;
import at.sintrum.fog.deploymentmanager.client.factory.DeploymentManagerClientFactory;
import at.sintrum.fog.metadatamanager.client.factory.MetadataManagerClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Michael Mittermayr on 24.05.2017.
 */
//@Service
public class MoveApplicationServiceImpl implements MoveApplicationService {

    private final EnvironmentInfoService environmentInfoService;
    private final DeploymentManagerClientFactory deploymentManagerClientFactory;
    private MetadataManagerClientFactory metadataManagerClientFactory;

    private final Logger LOGGER = LoggerFactory.getLogger(MoveApplicationServiceImpl.class);

    public MoveApplicationServiceImpl(EnvironmentInfoService environmentInfoService, DeploymentManagerClientFactory deploymentManagerClientFactory, MetadataManagerClientFactory metadataManagerClientFactory) {
        this.environmentInfoService = environmentInfoService;
        this.deploymentManagerClientFactory = deploymentManagerClientFactory;
        this.metadataManagerClientFactory = metadataManagerClientFactory;
    }

    @Override
    public void moveApplication(String targetIp, int targetPort) {
        String containerId = environmentInfoService.getOwnContainerId();
        String targetDeploymentMangerUrl = deploymentManagerClientFactory.buildUrl(targetIp, targetPort);

        String sourceDeploymentManagerUrl = environmentInfoService.getFogBaseUrl();

        //TODO: request app shutdown, persist stuff, notify about shutdown

        ApplicationManager applicationManagerClient = deploymentManagerClientFactory.createApplicationManagerClient(sourceDeploymentManagerUrl);
        applicationManagerClient.moveApplication(new ApplicationMoveRequest(containerId, targetDeploymentMangerUrl));
    }
}
