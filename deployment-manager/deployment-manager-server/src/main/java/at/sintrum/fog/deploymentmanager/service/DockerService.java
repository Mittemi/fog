package at.sintrum.fog.deploymentmanager.service;

import at.sintrum.fog.deploymentmanager.api.dto.*;

import java.util.List;

/**
 * Created by Michael Mittermayr on 20.05.2017.
 */
public interface DockerService {

    List<ContainerInfo> getContainers();

    List<ImageInfo> getImages();

    boolean isProtectedContainer(ContainerInfo containerInfo);

    ContainerInfo getContainerInfo(String containerId);

    boolean startContainer(String id);

    boolean stopContainer(String id);

    CreateContainerResult createContainer(CreateContainerRequest createContainerRequest);

    CommitContainerResult commitContainer(CommitContainerRequest commitContainerRequest);

    void tagImage(String imageId, String repository, String tag);

    void pullImage(PullImageRequest pullImageRequest);

    void pushImage(PushImageRequest pushImageRequest);

    void removeContainer(String containerId);
}
