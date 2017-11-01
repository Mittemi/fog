package at.sintrum.fog.deploymentmanager.service;

import at.sintrum.fog.deploymentmanager.api.dto.*;
import at.sintrum.fog.deploymentmanager.config.DeploymentManagerConfigProperties;
import at.sintrum.fog.deploymentmanager.utils.TarUtils;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.github.dockerjava.core.command.PushImageResultCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Michael Mittermayr on 20.05.2017.
 */
@Service
public class DockerServiceImpl implements DockerService {

    private final boolean disableRegistry;
    private final ThreadLocal<DockerClient> dockerClient;
    private DeploymentManagerConfigProperties deploymentManagerConfigProperties;
    private final DeploymentService deploymentService;

    private final Logger LOG = LoggerFactory.getLogger(DockerServiceImpl.class);

    public DockerServiceImpl(@Value("${DISABLE_REGISTRY:false}") boolean disableRegistry, DockerClientConfig dockerClientConfig, DeploymentManagerConfigProperties deploymentManagerConfigProperties, DeploymentService deploymentService) {
        this.disableRegistry = disableRegistry;
        this.dockerClient = ThreadLocal.withInitial(() -> DockerClientBuilder.getInstance(dockerClientConfig).build());

        this.deploymentManagerConfigProperties = deploymentManagerConfigProperties;
        this.deploymentService = deploymentService;
    }

    @Override
    public List<ContainerInfo> getContainers() {

        List<Container> listResult = dockerClient.get().listContainersCmd().withShowAll(true).exec();

        return listResult.stream().map(DockerServiceImpl::mapToDto).filter(x -> !isProtectedContainer(x)).collect(Collectors.toList());
    }

    @Override
    public List<ImageInfo> getImages() {
        List<Image> listResult = dockerClient.get().listImagesCmd().exec();

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

    @Override
    public ContainerInfo getContainerInfo(String containerId) {
        List<ContainerInfo> containerInfos = getContainers().stream().filter(x -> x.getId().startsWith(containerId)).collect(Collectors.toList());

        if (containerInfos.size() == 0) {
            return null;
        }
        if (containerInfos.size() > 1) {
            LOG.error("Container ID was not unique: " + containerId);
        }
        return containerInfos.get(0);
    }

    public ImageInfo getImageInfo(String imageId) {
        return getImages().stream().filter(x -> x.getId().equals(imageId)).findFirst().orElse(null);
    }

    public boolean startContainer(String id) {
        ContainerInfo containerInfo = getContainerInfo(id);
        if (containerInfo != null) {
            LOG.debug("Start Container: " + containerInfo);
            dockerClient.get().startContainerCmd(containerInfo.getId()).exec();
            return true;
        } else {
            LOG.warn("Start failed, container not found or protected: " + id);
            return false;
        }
    }

    public boolean stopContainer(String id) {
        ContainerInfo containerInfo = getContainerInfo(id);

        if (containerInfo != null) {
            LOG.debug("Stop Container: " + containerInfo);
            dockerClient.get().stopContainerCmd(containerInfo.getId()).exec();
            return true;
        } else {
            LOG.warn("Stop failed, container not found or protected: " + id);
            return false;
        }
    }

    @Override
    public CreateContainerResult createContainer(CreateContainerRequest createContainerRequest) {

        if (deploymentManagerConfigProperties.getMaxContainers() < getContainers().size()) {
            LOG.error("Can't start any new containers. This server has reached the max amount of containers.");
            return null;
        }

        try {
            CreateContainerCmd createContainerCmd = dockerClient.get().createContainerCmd(createContainerRequest.getImage());
            createContainerCmd.withTty(createContainerRequest.isWithTty());
            createContainerCmd.withEnv(createContainerRequest.getEnvironment());
            if (!StringUtils.isEmpty(createContainerRequest.getName())) {
                createContainerCmd.withName(createContainerRequest.getName());
            }
            if (!StringUtils.isEmpty(createContainerRequest.getRestartPolicy())) {
                createContainerCmd.withRestartPolicy(RestartPolicy.parse(createContainerRequest.getRestartPolicy()));
            }

            fillPortBindings(createContainerRequest, createContainerCmd);
            fillVolume(createContainerRequest, createContainerCmd);


            //TODO: container links
            //TODO: bind exposed ports dynamically

            CreateContainerResponse response = createContainerCmd.exec();
            return new CreateContainerResult(response.getId(), response.getWarnings(), createContainerRequest.getInstanceId());
        } catch (Exception ex) {
            LOG.error("Create container failed", ex);
            return null;
        }
    }

    @Override
    public CommitContainerResult commitContainer(CommitContainerRequest commitContainerRequest) {

        try {
            String temporaryTag = "latest_checkpoint";
            ContainerInfo containerInfo = getContainerInfo(commitContainerRequest.getContainerId());
            String repository = deploymentService.getRepositoryName(containerInfo.getImage());

            CommitCmd commitCmd = dockerClient.get().commitCmd(commitContainerRequest.getContainerId());
            commitCmd.withRepository(repository);
            commitCmd.withTag(temporaryTag);

            CommitContainerResult result = new CommitContainerResult(commitCmd.exec(), repository);
            for (String tag : commitContainerRequest.getTags()) {
                tagImage(repository + ":" + temporaryTag, repository, tag);
            }

            return result;
        } catch (Exception ex) {
            LOG.error("Commit container failed", ex);
            return null;
        }
    }

    @Override
    public boolean tagImage(String imageId, String repository, String tag) {
        try {
            TagImageCmd tagImageCmd = dockerClient.get().tagImageCmd(imageId, repository, tag);
            tagImageCmd.exec();
            return true;
        } catch (Exception ex) {
            LOG.error("Tag image failed", ex);
            return false;
        }
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
    public boolean pullImage(PullImageRequest pullImageRequest) {

        if (disableRegistry) {
            LOG.warn("Docker registry is disabled");
            return true;
        }

        try {
            String repository = deploymentService.getRepositoryName(pullImageRequest.getName());
            PullImageCmd pullImageCmd = dockerClient.get().pullImageCmd(repository);

            if (StringUtils.isEmpty(pullImageRequest.getTag())) {
                LOG.warn("Pull image without tag! Fallback to: latest");
                pullImageCmd.withTag("latest");
            } else {
                pullImageCmd.withTag(pullImageRequest.getTag());
            }

            if (!StringUtils.isEmpty(deploymentManagerConfigProperties.getRegistry())) {
                pullImageCmd.withRegistry(deploymentManagerConfigProperties.getRegistry());
            }

            pullImageCmd.exec(new PullImageResultCallback()).awaitSuccess();
            return true;
        } catch (Exception ex) {
            LOG.error("Pull image failed", ex);
            return false;
        }
    }

    @Override
    public boolean pushImage(PushImageRequest pushImageRequest) {

        if (disableRegistry) {
            LOG.warn("Docker registry is disabled");
            return true;
        }

        try {
            PushImageCmd pushImageCmd = dockerClient.get().pushImageCmd(pushImageRequest.getName());

            if (StringUtils.isEmpty(pushImageRequest.getTag())) {
                LOG.warn("Push image without tag!");
                pushImageCmd.withTag("latest");
            } else {
                pushImageCmd.withTag(pushImageRequest.getTag());
            }

            pushImageCmd.exec(new PushImageResultCallback()).awaitSuccess();
            return true;
        } catch (Exception ex) {
            LOG.error("Push image failed", ex);
            return false;
        }
    }

    @Override
    public boolean deleteImage(String id) {
        try {
            dockerClient.get().removeImageCmd(id).withNoPrune(true).exec();
            return true;
        } catch (Exception ex) {
            //don't care about result
        }
        return false;
    }

    @Override
    public boolean removeContainer(String containerId) {
        try {
            ContainerInfo containerInfo = getContainerInfo(containerId);

            if (containerInfo == null) {
                LOG.error("Container with id '" + containerId + "' not found");
            } else {
                if (containerInfo.isRunning()) {
                    LOG.error("Failed to remove container with id '" + containerId + "'. Stop container first.");
                } else {
                    dockerClient.get().removeContainerCmd(containerId).withRemoveVolumes(true).exec();
                }
            }
            return true;
        } catch (Exception ex) {
            LOG.error("Remove container failed", ex);
            return false;
        }
    }

    @Override
    public boolean copyOrMergeDirectory(String sourceContainerId, String sourceDirectory, String targetContainerId, String targetDirectory) {

        try {
            ContainerInfo sourceContainerInfo = getContainerInfo(sourceContainerId);
            ContainerInfo targetContainerInfo = getContainerInfo(targetContainerId);

            if (sourceContainerInfo == null) {
                LOG.error("Source container doesn't exist: " + sourceContainerId);
                return false;
            }
            if (targetContainerInfo == null) {
                LOG.error("Target container doesn't exist: " + targetContainerId);
                return false;
            }
            if (sourceContainerInfo.isRunning()) {
                LOG.error("Source container is running: " + sourceContainerId);
                return false;
            }
            if (targetContainerInfo.isRunning()) {
                LOG.error("Target container is running: " + targetContainerId);
                return false;
            }

            try (InputStream sourceFileStream = dockerClient.get().copyArchiveFromContainerCmd(sourceContainerInfo.getId(), sourceDirectory).withHostPath(sourceDirectory).exec()) {

                if (sourceFileStream == null) {
                    LOG.warn("Failed to get source files");
                    return false;
                }

                try (InputStream targetFileStream = dockerClient.get().copyArchiveFromContainerCmd(targetContainerInfo.getId(), targetDirectory).withHostPath(targetDirectory).exec()) {

                    if (targetFileStream == null) {
                        LOG.warn("Failed to get target files");
                        return false;
                    }

                    try (ByteArrayOutputStream mergedOutput = TarUtils.mergeArchives(sourceFileStream, targetFileStream)) {

                        if (mergedOutput == null) {
                            LOG.warn("Failed to merge archives");
                            return false;
                        }

                        byte[] resultingArchive = mergedOutput.toByteArray();

                        // save debug output
                        File resultFile = File.createTempFile("fog_result", ".tar");
                        try (FileOutputStream outputStream = new FileOutputStream(resultFile)) {
                            outputStream.write(resultingArchive);
                        }
                        LOG.debug("Result temp file can be found at: " + resultFile.getPath());

                        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(resultingArchive)) {
                            // workaround
                            // archive contains folder with the name of the last folder
                            String remotePath = getParentDir(targetDirectory);

                            dockerClient.get().copyArchiveToContainerCmd(targetContainerInfo.getId())
                                    .withTarInputStream(inputStream)
                                    .withNoOverwriteDirNonDir(false)
                                    .withDirChildrenOnly(true)
                                    .withRemotePath(remotePath)
                                    .exec();

                        }
                    }
                }
            }

            return true;
        } catch (Exception ex) {
            LOG.error("Failed to copy data between containers");
            return false;
        }

    }

    private String getParentDir(String targetDirectory) {
        String parent = targetDirectory;
        if (parent.endsWith("/")) {
            parent = parent.substring(0, parent.length() - 1);
        }

        return parent.substring(0, parent.lastIndexOf("/") + 1);
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
