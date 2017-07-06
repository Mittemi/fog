package at.sintrum.fog.deploymentmanager.service;

import at.sintrum.fog.clientcore.service.ShutdownApplicationService;
import at.sintrum.fog.core.service.EnvironmentInfoService;
import at.sintrum.fog.deploymentmanager.api.dto.*;
import at.sintrum.fog.deploymentmanager.client.factory.DeploymentManagerClientFactory;
import at.sintrum.fog.metadatamanager.api.ContainerMetadataApi;
import at.sintrum.fog.metadatamanager.api.ImageMetadataApi;
import at.sintrum.fog.metadatamanager.api.dto.DockerContainerMetadata;
import at.sintrum.fog.metadatamanager.api.dto.DockerImageMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collections;
import java.util.UUID;

/**
 * Created by Michael on 2017-06-29.
 */
@Service
public class ApplicationManagerServiceImpl implements ApplicationManagerService {

    private final DockerService dockerService;
    private final ImageMetadataApi imageMetadataApi;
    private final ContainerMetadataApi containerMetadataApi;
    private final DeploymentManagerClientFactory clientFactory;
    private final EnvironmentInfoService environmentInfoService;
    private final ShutdownApplicationService shutdownApplicationService;
    private final DeploymentService deploymentService;

    public ApplicationManagerServiceImpl(DockerService dockerService, ImageMetadataApi imageMetadataApi, ContainerMetadataApi containerMetadataApi, DeploymentManagerClientFactory clientFactory, EnvironmentInfoService environmentInfoService, ShutdownApplicationService shutdownApplicationService, DeploymentService deploymentService) {
        this.dockerService = dockerService;
        this.imageMetadataApi = imageMetadataApi;
        this.containerMetadataApi = containerMetadataApi;
        this.clientFactory = clientFactory;
        this.environmentInfoService = environmentInfoService;
        this.shutdownApplicationService = shutdownApplicationService;
        this.deploymentService = deploymentService;
    }

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationManagerServiceImpl.class);


    private DockerImageMetadata createStatefulServiceCheckpoint(@RequestBody ApplicationMoveRequest applicationMoveRequest, DockerImageMetadata imageMetadata) {
        String tag = "checkpoint_" + UUID.randomUUID().toString();

        CommitContainerResult checkpoint = dockerService.commitContainer(new CommitContainerRequest(applicationMoveRequest.getContainerId(), Collections.singletonList(tag)));
        dockerService.pushImage(new PushImageRequest(checkpoint.getImage(), tag));

        imageMetadata.setImage(checkpoint.getImage());
        imageMetadata.setTag(tag);
        imageMetadata.setId(null);  //create new/no update
        imageMetadata = imageMetadataApi.store(imageMetadata);
        return imageMetadata;
    }

    private FogOperationResult moveContainerToRemote(ApplicationMoveRequest applicationMoveRequest, DockerImageMetadata imageMetadata) {
        at.sintrum.fog.deploymentmanager.client.api.ApplicationManager applicationManagerClient = clientFactory.createApplicationManagerClient(applicationMoveRequest.getTargetFog());

        FogOperationResult fogOperationResult = applicationManagerClient.requestApplicationStart(new ApplicationStartRequest(imageMetadata.getId()));

        if (fogOperationResult.isSuccessful()) {
            containerMetadataApi.delete(applicationMoveRequest.getContainerId());
            dockerService.removeContainer(applicationMoveRequest.getContainerId());
        } else {
            LOG.info("Move container failed. Restart original container");
            dockerService.startContainer(applicationMoveRequest.getContainerId());
            return new FogOperationResult(applicationMoveRequest.getContainerId(), false, environmentInfoService.getFogBaseUrl(), "move failed, recovered");
        }

        return fogOperationResult;
    }

    @Async
    @Override
    public FogOperationResult start(ApplicationStartRequest applicationStartRequest) {
        String metadataId = applicationStartRequest.getMetadataId();

        LOG.info("Request application start: " + metadataId);

        DockerImageMetadata imageMetadata = imageMetadataApi.getById(metadataId);

        if (imageMetadata == null) {
            LOG.error("Image metadata missing for: " + metadataId);
            return new FogOperationResult(null, false, environmentInfoService.getFogBaseUrl(), "Image metadata missing.");
        } else {
            dockerService.pullImage(new PullImageRequest(imageMetadata.getImage(), imageMetadata.getTag()));

            CreateContainerRequest createContainerRequest = deploymentService.buildCreateContainerRequest(imageMetadata);

            CreateContainerResult container = dockerService.createContainer(createContainerRequest);

            if (container.getWarnings() != null && container.getWarnings().length > 0) {
                LOG.warn("Warnings during container creation. ID: " + container.getId() + "\n" + String.join("\n, ", container.getWarnings()));
            }

            DockerContainerMetadata containerMetadata = new DockerContainerMetadata(container.getId(), imageMetadata.getId());
            containerMetadataApi.store(containerMetadata);

            //TODO: logging
            dockerService.startContainer(container.getId());

            return new FogOperationResult(container.getId(), true, environmentInfoService.getFogBaseUrl());
        }
    }

    @Async
    @Override
    public FogOperationResult move(ApplicationMoveRequest applicationMoveRequest) {
        ContainerInfo containerInfo = dockerService.getContainerInfo(applicationMoveRequest.getContainerId());

        if (containerInfo == null) {
            LOG.warn("Can't move container. Unknown container '" + applicationMoveRequest.getContainerId() + "'");
            return new FogOperationResult(null, false, environmentInfoService.getFogBaseUrl(), "unknown container");
        } else {
            DockerContainerMetadata containerMetadata = containerMetadataApi.getById(containerInfo.getId());

            if (containerMetadata == null) {
                LOG.error("ContainerMetadata missing for container: " + containerInfo.getId());
                return new FogOperationResult(null, false, environmentInfoService.getFogBaseUrl(), "missing container metadata");
            } else {

                try {
                    if (StringUtils.isEmpty(applicationMoveRequest.getApplicationUrl())) {
                        LOG.warn("ApplicationURL missing. Can't send shutdown request.");
                    } else {
                        shutdownApplicationService.shutdown(applicationMoveRequest.getApplicationUrl());
                        Thread.sleep(1000);
                    }
                } catch (Exception ex) {
                    LOG.error("Request application shutdown failed with: " + ex);
                }
                dockerService.stopContainer(applicationMoveRequest.getContainerId());

                DockerImageMetadata imageMetadata = imageMetadataApi.getById(containerMetadata.getImageMetadataId());

                if (imageMetadata == null) {
                    LOG.error("ImageMetadata was null for ID: " + containerMetadata.getImageMetadataId());
                    return new FogOperationResult(containerMetadata.getContainerId(), false, environmentInfoService.getFogBaseUrl(), "image metadata missing");
                }

                if (!imageMetadata.isStateless()) {
                    imageMetadata = createStatefulServiceCheckpoint(applicationMoveRequest, imageMetadata);
                }

                return moveContainerToRemote(applicationMoveRequest, imageMetadata);
            }
        }
    }
}
