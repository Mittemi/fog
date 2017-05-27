package at.sintrum.fog.application.core.service;

import at.sintrum.fog.core.service.EnvironmentInfoService;
import at.sintrum.fog.deploymentmanager.api.dto.CommitContainerRequest;
import at.sintrum.fog.deploymentmanager.api.dto.CommitContainerResult;
import at.sintrum.fog.deploymentmanager.api.dto.ContainerInfo;
import at.sintrum.fog.deploymentmanager.client.api.ContainerManager;
import at.sintrum.fog.deploymentmanager.client.api.ImageManager;
import at.sintrum.fog.deploymentmanager.client.factory.DeploymentManagerClientFactory;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.*;

/**
 * Created by Michael Mittermayr on 24.05.2017.
 */
@Service
public class MoveApplicationServiceImpl implements MoveApplicationService {

    private final EnvironmentInfoService environmentInfoService;
    private final DeploymentManagerClientFactory clientFactory;

    private final Logger LOGGER = LoggerFactory.getLogger(MoveApplicationServiceImpl.class);

    public MoveApplicationServiceImpl(EnvironmentInfoService environmentInfoService, DeploymentManagerClientFactory clientFactory) {
        this.environmentInfoService = environmentInfoService;
        this.clientFactory = clientFactory;
    }

    @Override
    public void moveApplication(String targetIp, int targetPort) {
        String containerId = environmentInfoService.getOwnContainerId();
        containerId = "8d38361ab939";
        String targetDeploymentMangerUrl = clientFactory.buildUrl(targetIp, targetPort);
        //TODO: check if container exists, check if it is running at all

        String sourceDeploymentManagerUrl = environmentInfoService.getDeploymentManagerUrl();
        ContainerManager sourceContainerManager = clientFactory.createContainerManagerClient(sourceDeploymentManagerUrl);
        ImageManager sourceImageManager = clientFactory.createImageManagerClient(sourceDeploymentManagerUrl);

        ContainerManager targetContainerManger = clientFactory.createContainerManagerClient(targetDeploymentMangerUrl);
        ImageManager targetImageManager = clientFactory.createImageManagerClient(targetDeploymentMangerUrl);

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
