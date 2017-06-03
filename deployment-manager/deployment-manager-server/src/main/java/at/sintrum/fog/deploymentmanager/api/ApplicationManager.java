package at.sintrum.fog.deploymentmanager.api;

import at.sintrum.fog.deploymentmanager.api.dto.*;
import at.sintrum.fog.deploymentmanager.service.DeploymentService;
import at.sintrum.fog.deploymentmanager.service.DockerService;
import at.sintrum.fog.metadatamanager.api.dto.DockerImageMetadata;
import at.sintrum.fog.metadatamanager.client.api.ImageMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

/**
 * Created by Michael Mittermayr on 31.05.2017.
 */
@RestController
public class ApplicationManager implements ApplicationManagerApi {

    private final Logger LOG = LoggerFactory.getLogger(ApplicationManager.class);

    private final DockerService dockerService;
    private ImageMetadata applicationMetadata;
    private DeploymentService deploymentService;

    //TODO: move some stuff to a service


    public ApplicationManager(DockerService dockerService, ImageMetadata applicationMetadata, DeploymentService deploymentService) {
        this.dockerService = dockerService;
        this.applicationMetadata = applicationMetadata;
        this.deploymentService = deploymentService;
    }


    @Override
    public void requestApplicationStart(@RequestBody ApplicationStartRequest applicationStartRequest) {

        String metadataId = applicationStartRequest.getMetadataId();

        LOG.info("Request application start: " + metadataId);

        DockerImageMetadata imageMetadata = applicationMetadata.getById(metadataId);

        if (imageMetadata == null) {
            LOG.error("Image metadata missing for: " + metadataId);
        } else {
            dockerService.pullImage(new PullImageRequest(imageMetadata.getImage(), imageMetadata.getTag()));

            CreateContainerRequest createContainerRequest = deploymentService.buildCreateContainerRequest(imageMetadata);

            CreateContainerResult container = dockerService.createContainer(createContainerRequest);

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
            CommitContainerResult checkpoint = dockerService.commitContainer(new CommitContainerRequest(applicationMoveRequest.getContainerId(), Collections.singletonList("checkpoint")));


        }

    }


}
