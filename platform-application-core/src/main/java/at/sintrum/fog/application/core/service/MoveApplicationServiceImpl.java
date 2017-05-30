package at.sintrum.fog.application.core.service;

import at.sintrum.fog.core.service.EnvironmentInfoService;
import at.sintrum.fog.deploymentmanager.api.dto.ContainerInfo;
import at.sintrum.fog.deploymentmanager.client.api.ContainerManager;
import at.sintrum.fog.deploymentmanager.client.api.ImageManager;
import at.sintrum.fog.deploymentmanager.client.factory.DeploymentManagerClientFactory;
import at.sintrum.fog.metadatamanager.client.factory.MetadataManagerClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by Michael Mittermayr on 24.05.2017.
 */
@Service
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
        containerId = "8d38361ab939";
        String targetDeploymentMangerUrl = deploymentManagerClientFactory.buildUrl(targetIp, targetPort);
        //TODO: check if container exists, check if it is running at all

        String sourceDeploymentManagerUrl = environmentInfoService.getDeploymentManagerUrl();
        ContainerManager sourceContainerManager = deploymentManagerClientFactory.createContainerManagerClient(sourceDeploymentManagerUrl);
        ImageManager sourceImageManager = deploymentManagerClientFactory.createImageManagerClient(sourceDeploymentManagerUrl);

        ContainerManager targetContainerManger = deploymentManagerClientFactory.createContainerManagerClient(targetDeploymentMangerUrl);
        ImageManager targetImageManager = deploymentManagerClientFactory.createImageManagerClient(targetDeploymentMangerUrl);

        java.util.List<ContainerInfo> containers = sourceContainerManager.getContainers();

        //notifyApplicationAboutShutdown(containerId, sourceContainerManager);
        stopContainer(containerId, sourceContainerManager);
        transferLocalState(containerId, sourceContainerManager, sourceImageManager);


    }

    private void stopContainer(String containerId, ContainerManager containerManagerClient) {
        //TODO: container might stopped already

        if (containerManagerClient.stopContainer(containerId)) {
            LOGGER.info("Container '" + containerId + "' stopped.");
        } else {
            LOGGER.error("Failed to stop container '" + containerId + "'.");
        }

        //TODO: delete container
    }

    private void notifyApplicationAboutShutdown(String containerId, ContainerManager containerManagerClient) {
        //TODO: notify target application about shutdown

        try {
            Thread.sleep(3000);
        } catch (InterruptedException ignored) {
        }
    }

    private void transferLocalState(String containerId, ContainerManager containerManagerClient, ImageManager sourceImageManager) {
        //CommitContainerResult current = containerManagerClient.commitContainer(new CommitContainerRequest(containerId, Collections.singletonList("current")));
        //TODO: transfer state
    }
}
