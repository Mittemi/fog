package at.sintrum.fog.deploymentmanager.api;

import at.sintrum.fog.deploymentmanager.api.dto.*;
import at.sintrum.fog.deploymentmanager.client.factory.DeploymentManagerClientFactory;
import at.sintrum.fog.deploymentmanager.service.DeploymentService;
import at.sintrum.fog.deploymentmanager.service.DockerService;
import at.sintrum.fog.metadatamanager.api.ContainerMetadataApi;
import at.sintrum.fog.metadatamanager.api.ImageMetadataApi;
import at.sintrum.fog.metadatamanager.api.dto.DockerContainerMetadata;
import at.sintrum.fog.metadatamanager.api.dto.DockerImageMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.UUID;

/**
 * Created by Michael Mittermayr on 31.05.2017.
 */
@RestController
public class ApplicationManager implements ApplicationManagerApi {

    private final Logger LOG = LoggerFactory.getLogger(ApplicationManager.class);

    private final DockerService dockerService;
    private final ImageMetadataApi imageMetadataApi;
    private final ContainerMetadataApi containerMetadataApi;
    private final DeploymentService deploymentService;
    private final DeploymentManagerClientFactory clientFactory;

    public ApplicationManager(DockerService dockerService, ImageMetadataApi imageMetadataApi, ContainerMetadataApi containerMetadataApi, DeploymentService deploymentService, DeploymentManagerClientFactory clientFactory) {
        this.dockerService = dockerService;
        this.imageMetadataApi = imageMetadataApi;
        this.containerMetadataApi = containerMetadataApi;
        this.deploymentService = deploymentService;
        this.clientFactory = clientFactory;
    }


    @Override
    public void requestApplicationStart(@RequestBody ApplicationStartRequest applicationStartRequest) {

        String metadataId = applicationStartRequest.getMetadataId();

        LOG.info("Request application start: " + metadataId);

        DockerImageMetadata imageMetadata = imageMetadataApi.getById(metadataId);

        if (imageMetadata == null) {
            LOG.error("Image metadata missing for: " + metadataId);
        } else {
            dockerService.pullImage(new PullImageRequest(imageMetadata.getImage(), imageMetadata.getTag()));

            CreateContainerRequest createContainerRequest = deploymentService.buildCreateContainerRequest(imageMetadata);

            CreateContainerResult container = dockerService.createContainer(createContainerRequest);

            if (container.getWarnings().length > 0) {
                LOG.warn("Warnings during container creation. ID: " + container.getId() + "\n" + String.join("\n, ", container.getWarnings()));
            }

            DockerContainerMetadata containerMetadata = new DockerContainerMetadata(container.getId(), imageMetadata.getId());
            containerMetadataApi.store(containerMetadata);

            //TODO: logging
            dockerService.startContainer(container.getId());
        }
    }

    @Override
    public void moveApplication(@RequestBody ApplicationMoveRequest applicationMoveRequest) {

        ContainerInfo containerInfo = dockerService.getContainerInfo(applicationMoveRequest.getContainerId());

        if (containerInfo == null) {
            LOG.warn("Can't move container. Unknown container '" + applicationMoveRequest.getContainerId() + "'");
        } else {
            String tag = "checkpoint_" + UUID.randomUUID().toString();
            CommitContainerResult checkpoint = dockerService.commitContainer(new CommitContainerRequest(applicationMoveRequest.getContainerId(), Collections.singletonList(tag)));
            dockerService.pushImage(new PushImageRequest(checkpoint.getImage(), tag));

            DockerContainerMetadata containerMetadata = containerMetadataApi.getById(containerInfo.getId());

            if (containerMetadata == null) {
                LOG.error("ContainerMetadata missing for container: " + containerInfo.getId());
            } else {
                DockerImageMetadata imageMetadata = imageMetadataApi.getById(containerMetadata.getImageMetadataId());
                imageMetadata.setImage(checkpoint.getImage());      //TODO: check if required!
                imageMetadata.setTag(tag);
                imageMetadata.setId(null);  //create new/no update
                imageMetadata = imageMetadataApi.store(imageMetadata);

                at.sintrum.fog.deploymentmanager.client.api.ApplicationManager applicationManagerClient = clientFactory.createApplicationManagerClient(applicationMoveRequest.getTargetFog());

                applicationManagerClient.requestApplicationStart(new ApplicationStartRequest(imageMetadata.getId()));
            }

            //TODO: remove container, free resources
            //dockerService.removeContainer
        }
    }
}
