package at.sintrum.fog.deploymentmanager.service;

import at.sintrum.fog.deploymentmanager.api.dto.*;
import at.sintrum.fog.deploymentmanager.config.DeploymentManagerConfigProperties;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.command.PushImageCmd;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ContainerPort;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.github.dockerjava.core.command.PushImageResultCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Michael Mittermayr on 20.05.2017.
 */
@Service
public class DockerServiceImpl implements DockerService {

    private final DockerClient dockerClient;
    private DeploymentManagerConfigProperties deploymentManagerConfigProperties;

    private final Logger LOG = LoggerFactory.getLogger(DockerServiceImpl.class);

    public DockerServiceImpl(DockerClient dockerClient, DeploymentManagerConfigProperties deploymentManagerConfigProperties) {
        this.dockerClient = dockerClient;
        this.deploymentManagerConfigProperties = deploymentManagerConfigProperties;
    }

    @Override
    public List<ContainerInfo> getContainers() {

        List<Container> listResult = dockerClient.listContainersCmd().exec();

        return listResult.stream().map(DockerServiceImpl::mapToDto).filter(x -> !isProtectedContainer(x)).collect(Collectors.toList());
    }

    @Override
    public List<ImageInfo> getImages() {
        List<Image> listResult = dockerClient.listImagesCmd().exec();

        return listResult.stream().map(DockerServiceImpl::mapImageToDto).collect(Collectors.toList());
    }

    private static ImageInfo mapImageToDto(Image image) {

        ImageInfo result = new ImageInfo(image.getId(), Arrays.asList(image.getRepoTags()));

        return result;
    }

    @Override
    public boolean isProtectedContainer(ContainerInfo containerInfo) {

        return deploymentManagerConfigProperties.getProtectedContainers().stream().anyMatch(x -> x.equals(containerInfo.getImage()));
    }

    public boolean startContainer(String id) {
        Optional<ContainerInfo> first = getContainers().stream().filter(x -> x.getId().equals(id)).findFirst();

        if (first.isPresent()) {
            ContainerInfo containerInfo = first.get();
            LOG.debug("Start Container: " + containerInfo);
            dockerClient.startContainerCmd(containerInfo.getId()).exec();
            return true;
        } else {
            LOG.warn("Start failed, container not found or protected: " + id);
            return false;
        }
    }

    public boolean stopContainer(String id) {
        Optional<ContainerInfo> first = getContainers().stream().filter(x -> x.getId().equals(id)).findFirst();

        if (first.isPresent()) {
            ContainerInfo containerInfo = first.get();
            LOG.debug("Stop Container: " + containerInfo);
            dockerClient.stopContainerCmd(containerInfo.getId()).exec();
            return true;
        } else {
            LOG.warn("Stop failed, container not found or protected: " + id);
            return false;
        }
    }

    @Override
    public CreateContainerResult createContainer(CreateContainerRequest createContainerRequest) {

        CreateContainerResponse response = dockerClient.createContainerCmd(createContainerRequest.getImage()).exec();

        return new CreateContainerResult(response.getId(), response.getWarnings());
    }

    @Override
    public void pullImage(PullImageRequest pullImageRequest) {
        PullImageCmd pullImageCmd = dockerClient.pullImageCmd(pullImageRequest.getName());

        if (!StringUtils.isEmpty(pullImageRequest.getTag())) {
            pullImageCmd.withTag(pullImageCmd.getTag());
        }

        pullImageCmd.exec(new PullImageResultCallback()).awaitSuccess();
    }

    @Override
    public void pushImage(PushImageRequest pushImageRequest) {
        PushImageCmd pushImageCmd = dockerClient.pushImageCmd(pushImageRequest.getName());

        if (!StringUtils.isEmpty(pushImageRequest.getTag())) {
            pushImageCmd.withTag(pushImageRequest.getTag());
        }

        pushImageCmd.exec(new PushImageResultCallback()).awaitSuccess();
    }

    private static ContainerInfo mapToDto(Container container) {
        ContainerInfo containerInfo = new ContainerInfo(container.getId(), container.getImageId(), container.getImage(), isRunning(container));
        fillPorts(containerInfo, container);
        return containerInfo;
    }

    private static void fillPorts(ContainerInfo containerInfo, Container container) {
        for (ContainerPort containerPort : container.getPorts()) {
            containerInfo.getPortInfos().add(new PortInfo(containerPort.getIp(), containerPort.getPrivatePort(), containerPort.getPublicPort(), containerPort.getType()));
        }
    }

    private static boolean isRunning(Container container) {

        return container.getStatus().startsWith("Up");
    }
}
