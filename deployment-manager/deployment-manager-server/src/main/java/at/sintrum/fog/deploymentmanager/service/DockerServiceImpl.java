package at.sintrum.fog.deploymentmanager.service;

import at.sintrum.fog.deploymentmanager.api.dto.*;
import at.sintrum.fog.deploymentmanager.config.DeploymentManagerConfigProperties;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.github.dockerjava.core.command.PushImageResultCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.LinkedList;
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

        List<Container> listResult = dockerClient.listContainersCmd().withShowAll(true).exec();

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
        //TODO: check if single container only (exactly 1)
        Optional<ContainerInfo> first = getContainers().stream().filter(x -> x.getId().startsWith(id)).findFirst();

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
        //TODO: check if single container only (exactly 1)
        Optional<ContainerInfo> first = getContainers().stream().filter(x -> x.getId().startsWith(id)).findFirst();

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

        CreateContainerCmd createContainerCmd = dockerClient.createContainerCmd(createContainerRequest.getImage());
        createContainerCmd.withTty(createContainerRequest.isWithTty());
        createContainerCmd.withEnv(createContainerRequest.getEnvironment());
        if (!StringUtils.isEmpty(createContainerRequest.getRestartPolicy())) {
            createContainerCmd.withRestartPolicy(RestartPolicy.parse(createContainerRequest.getRestartPolicy()));
        }

        fillPortBindings(createContainerRequest, createContainerCmd);
        fillVolume(createContainerRequest, createContainerCmd);


        //TODO: container links
        //TODO: bind exposed ports dynamically

        CreateContainerResponse response = createContainerCmd.exec();
        return new CreateContainerResult(response.getId(), response.getWarnings());
    }

    @Override
    public CommitContainerResult commitContainer(CommitContainerRequest commitContainerRequest) {
        CommitCmd commitCmd = dockerClient.commitCmd(commitContainerRequest.getContainerId());
        return new CommitContainerResult(commitCmd.exec());
    }

    @Override
    public void tagImage(String imageId, String repository, String tag) {
        dockerClient.tagImageCmd(imageId, repository, tag).exec();
    }

    private void fillPortBindings(CreateContainerRequest createContainerRequest, CreateContainerCmd createContainerCmd) {
        List<PortBinding> portBindings = new LinkedList<>();
        List<ExposedPort> exposedPorts = new LinkedList<>();
        for (PortInfo portInfo : createContainerRequest.getPortInfos()) {
            Ports.Binding binding;
            if (StringUtils.isEmpty(portInfo.getIp())) {
                binding = Ports.Binding.bindPort(portInfo.getHostPort());
            } else {
                binding = Ports.Binding.bindIpAndPort(portInfo.getIp(), portInfo.getHostPort());
            }

            ExposedPort exposedPort;
            if (StringUtils.isEmpty(portInfo.getType())) {
                exposedPort = new ExposedPort(portInfo.getContainerPort());
            } else {
                exposedPort = new ExposedPort(portInfo.getContainerPort(), InternetProtocol.valueOf(portInfo.getType()));
            }

            PortBinding portBinding = new PortBinding(binding, exposedPort);
            exposedPorts.add(exposedPort);
            portBindings.add(portBinding);
        }
        createContainerCmd.withExposedPorts(exposedPorts);
        createContainerCmd.withPortBindings(portBindings);
    }

    private void fillVolume(CreateContainerRequest createContainerRequest, CreateContainerCmd createContainerCmd) {
        List<Volume> volumes = new LinkedList<>();
        List<Bind> binds = new LinkedList<>();
        for (VolumeInfo volumeInfo : createContainerRequest.getVolumes()) {
            Volume volume = new Volume(volumeInfo.getContainerDir());
            volumes.add(volume);
            if (!StringUtils.isEmpty(volumeInfo.getHostDir())) {
                Bind bind = new Bind(volumeInfo.getHostDir(), volume);
                binds.add(bind);
            }
        }
        createContainerCmd.withVolumes(volumes);
        createContainerCmd.withBinds(binds);
    }

    @Override
    public void pullImage(PullImageRequest pullImageRequest) {
        PullImageCmd pullImageCmd = dockerClient.pullImageCmd(pullImageRequest.getName());

        if (!StringUtils.isEmpty(pullImageRequest.getTag())) {
            LOG.warn("Pull image without tag!");
            pullImageCmd.withTag(pullImageCmd.getTag());
        }

        pullImageCmd.exec(new PullImageResultCallback()).awaitSuccess();
    }

    @Override
    public void pushImage(PushImageRequest pushImageRequest) {
        PushImageCmd pushImageCmd = dockerClient.pushImageCmd(pushImageRequest.getName());

        if (!StringUtils.isEmpty(pushImageRequest.getTag())) {
            LOG.warn("Push image without tag!");
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
