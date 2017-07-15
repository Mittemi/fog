package at.sintrum.fog.deploymentmanager.service;

import at.sintrum.fog.applicationhousing.api.dto.AppIdentification;
import at.sintrum.fog.applicationhousing.api.dto.AppUpdateInfo;
import at.sintrum.fog.applicationhousing.client.api.AppEvolution;
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
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.Future;

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
    private final MetadataServiceImpl metadataService;
    private final AppEvolution appEvolutionClient;

    public ApplicationManagerServiceImpl(DockerService dockerService,
                                         ImageMetadataApi imageMetadataApi,
                                         ContainerMetadataApi containerMetadataApi,
                                         DeploymentManagerClientFactory clientFactory,
                                         EnvironmentInfoService environmentInfoService,
                                         ShutdownApplicationService shutdownApplicationService,
                                         DeploymentService deploymentService,
                                         MetadataServiceImpl metadataService,
                                         AppEvolution appEvolutionClient) {
        this.dockerService = dockerService;
        this.imageMetadataApi = imageMetadataApi;
        this.containerMetadataApi = containerMetadataApi;
        this.clientFactory = clientFactory;
        this.environmentInfoService = environmentInfoService;
        this.shutdownApplicationService = shutdownApplicationService;
        this.deploymentService = deploymentService;
        this.metadataService = metadataService;
        this.appEvolutionClient = appEvolutionClient;
    }

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationManagerServiceImpl.class);


    private FogOperationResult moveContainerToRemote(ApplicationMoveRequest applicationMoveRequest, DockerImageMetadata imageMetadata) {
        at.sintrum.fog.deploymentmanager.client.api.ApplicationManager applicationManagerClient = clientFactory.createApplicationManagerClient(applicationMoveRequest.getTargetFog());
        FogOperationResult fogOperationResult = null;

        try {
            ApplicationStartRequest startRequest = new ApplicationStartRequest(imageMetadata.getId());
            startRequest.setSkipPull(false);

            fogOperationResult = applicationManagerClient.requestApplicationStart(startRequest);
        } catch (Exception ex) {
            LOG.error("Remote deployment manager call failed", ex);
        }

        boolean isSuccessful = fogOperationResult != null && fogOperationResult.isSuccessful();
        String originalContainerId = applicationMoveRequest.getContainerId();
        if (!finalizeReplaceContainerOperation(isSuccessful, originalContainerId)) {
            return new FogOperationResult(originalContainerId, false, environmentInfoService.getFogBaseUrl(), "move failed, recovered");
        }
        return fogOperationResult;
    }

    private boolean finalizeReplaceContainerOperation(boolean isSuccessful, String originalContainerId) {
        if (isSuccessful) {
            containerMetadataApi.delete(originalContainerId);
            if (!dockerService.removeContainer(originalContainerId)) {
                LOG.error("Failed to delete moved container. Unnecessary resources!");
            }
            return true;
        } else {
            LOG.info("Move container failed. Restart original container");
            if (!dockerService.startContainer(originalContainerId)) {
                LOG.error("Houston, we have a problem! Failed to restart original container!");
            }
            return false;
        }
    }

    @Async
    @Override
    public Future<FogOperationResult> start(ApplicationStartRequest applicationStartRequest) {
        String metadataId = applicationStartRequest.getMetadataId();

        LOG.info("Request application start: " + metadataId);

        DockerImageMetadata imageMetadata = imageMetadataApi.getById(metadataId);

        if (imageMetadata == null) {
            LOG.error("Image metadata missing for: " + metadataId);
            return new AsyncResult<>(new FogOperationResult(null, false, environmentInfoService.getFogBaseUrl(), "Image metadata missing."));
        } else {
            FogOperationResult fogOperationResult = createContainer(applicationStartRequest, imageMetadata);

            if (!fogOperationResult.isSuccessful()) {
                return new AsyncResult<>(fogOperationResult);
            }

            //TODO: logging
            if (!dockerService.startContainer(fogOperationResult.getContainerId())) {
                return new AsyncResult<>(new FogOperationResult(fogOperationResult.getContainerId(), false, environmentInfoService.getFogBaseUrl(), "Failed to start container"));
            }
            //   metadataService.finishStartup(applicationStartRequest);
            return new AsyncResult<>(new FogOperationResult(fogOperationResult.getContainerId(), true, environmentInfoService.getFogBaseUrl()));
        }
    }

    private FogOperationResult createContainer(ApplicationStartRequest applicationStartRequest, DockerImageMetadata imageMetadata) {
        if (pullImage(applicationStartRequest, imageMetadata)) {
            return new FogOperationResult(null, false, environmentInfoService.getFogBaseUrl(), "Failed to pull image");
        }

        CreateContainerRequest createContainerRequest = deploymentService.buildCreateContainerRequest(imageMetadata);

        if (environmentInfoService.isCloud()) {
            LOG.info("Cloud environment: Start application in standby mode!");
            deploymentService.enableServiceProfile(createContainerRequest, "standby");
        }

        CreateContainerResult container = dockerService.createContainer(createContainerRequest);

        if (container == null) {
            return new FogOperationResult(null, false, environmentInfoService.getFogBaseUrl(), "Failed to create container");
        }

        if (container.getWarnings() != null && container.getWarnings().length > 0) {
            LOG.warn("Warnings during container creation. ID: " + container.getId() + "\n" + String.join("\n, ", container.getWarnings()));
        }

        DockerContainerMetadata containerMetadata = new DockerContainerMetadata(container.getId(), imageMetadata.getId());
        containerMetadataApi.store(containerMetadata);

        return new FogOperationResult(container.getId(), true, environmentInfoService.getFogBaseUrl());
    }

    private boolean pullImage(ApplicationStartRequest applicationStartRequest, DockerImageMetadata imageMetadata) {
        if (!applicationStartRequest.isSkipPull()) {
            if (!dockerService.pullImage(new PullImageRequest(imageMetadata.getImage(), imageMetadata.getTag()))) {
                return true;
            }
        } else {
            LOG.info("Skipped to pull image");
        }
        return false;
    }

    @Async
    @Override
    public Future<FogOperationResult> move(ApplicationMoveRequest applicationMoveRequest) {
        ContainerInfo containerInfo = dockerService.getContainerInfo(applicationMoveRequest.getContainerId());

        if (containerInfo == null) {
            LOG.warn("Can't move container. Unknown container '" + applicationMoveRequest.getContainerId() + "'");
            return new AsyncResult<>(new FogOperationResult(null, false, environmentInfoService.getFogBaseUrl(), "unknown container"));
        } else {
            DockerContainerMetadata containerMetadata = containerMetadataApi.getById(containerInfo.getId());

            if (containerMetadata == null) {
                LOG.error("ContainerMetadata missing for container: " + containerInfo.getId());
                return new AsyncResult<>(new FogOperationResult(null, false, environmentInfoService.getFogBaseUrl(), "missing container metadata"));
            } else {

                if (!stopApplication(applicationMoveRequest.getApplicationUrl(), applicationMoveRequest.getContainerId())) {
                    return new AsyncResult<>(new FogOperationResult(containerInfo.getId(), false, environmentInfoService.getFogBaseUrl(), "Failed to stop container"));
                }
                DockerImageMetadata imageMetadata = imageMetadataApi.getById(containerMetadata.getImageMetadataId());

                if (imageMetadata == null) {
                    LOG.error("ImageMetadata was null for ID: " + containerMetadata.getImageMetadataId());
                    return new AsyncResult<>(new FogOperationResult(containerMetadata.getContainerId(), false, environmentInfoService.getFogBaseUrl(), "Image metadata missing"));
                }

                if (!imageMetadata.isStateless()) {
                    String tag = "checkpoint_" + UUID.randomUUID().toString();

                    CommitContainerResult checkpoint = dockerService.commitContainer(new CommitContainerRequest(applicationMoveRequest.getContainerId(), Collections.singletonList(tag)));

                    if (checkpoint == null) {
                        return new AsyncResult<>(new FogOperationResult(containerInfo.getId(), false, environmentInfoService.getFogBaseUrl(), "Failed to create checkpoint"));
                    }

                    if (!dockerService.pushImage(new PushImageRequest(checkpoint.getImage(), tag))) {
                        return new AsyncResult<>(new FogOperationResult(containerInfo.getId(), false, environmentInfoService.getFogBaseUrl(), "Failed to push checkpoint"));
                    }

                    imageMetadata.setImage(checkpoint.getImage());
                    imageMetadata.setTag(tag);
                    imageMetadata.setId(null);  //create new/no update
                    imageMetadata = imageMetadataApi.store(imageMetadata);
                }

                return new AsyncResult<>(moveContainerToRemote(applicationMoveRequest, imageMetadata));
            }
        }
    }

    private boolean stopApplication(String applicationUrl, String containerId) {
        try {
            //TODO: check if shutdown is a good choice. we might need to send some do your stuff make yourself ready to move request
            if (StringUtils.isEmpty(applicationUrl)) {
                LOG.warn("ApplicationURL missing. Can't send shutdown request.");
            } else {
                shutdownApplicationService.shutdown(applicationUrl);
                Thread.sleep(1000);
            }
        } catch (Exception ex) {
            LOG.error("Request application shutdown failed with: " + ex);
        }
        return dockerService.stopContainer(containerId);
    }

    @Async
    @Override
    public Future<FogOperationResult> upgrade(ApplicationUpgradeRequest applicationUpgradeRequest) {

        ContainerInfo containerInfo = dockerService.getContainerInfo(applicationUpgradeRequest.getContainerId());

        if (containerInfo == null) {
            LOG.warn("Can't upgrade container. Unknown container '" + applicationUpgradeRequest.getContainerId() + "'");
            return new AsyncResult<>(new FogOperationResult(null, false, environmentInfoService.getFogBaseUrl(), "unknown container"));
        } else {
            DockerContainerMetadata containerMetadata = containerMetadataApi.getById(containerInfo.getId());

            if (containerMetadata == null) {
                LOG.error("ContainerMetadata missing for container: " + containerInfo.getId());
                return new AsyncResult<>(new FogOperationResult(null, false, environmentInfoService.getFogBaseUrl(), "missing container metadata"));
            } else {

                // get update infos
                AppUpdateInfo appUpdateInfo = appEvolutionClient.checkForUpdate(new AppIdentification(containerMetadata.getImageMetadataId()));
                if (!appUpdateInfo.isUpdateRequired()) {
                    LOG.warn("There is no update for container: " + containerInfo.getId());
                    return new AsyncResult<>(new FogOperationResult(containerInfo.getId(), false, environmentInfoService.getFogBaseUrl(), "No update available for this application"));
                }

                // metadata: new version
                DockerImageMetadata imageMetadata = imageMetadataApi.getById(appUpdateInfo.getImageMetadataId());
                if (imageMetadata == null) {
                    LOG.error("Image metadata for new app version missing!");
                    return new AsyncResult<>(new FogOperationResult(containerInfo.getId(), false, environmentInfoService.getFogBaseUrl(), "Image metadata for new app version missing"));
                }

                // create image for new version
                FogOperationResult fogOperationResult = createContainer(new ApplicationStartRequest(imageMetadata.getId()), imageMetadata);
                if (!fogOperationResult.isSuccessful()) {
                    return new AsyncResult<>(fogOperationResult);
                }

                //copy data
                //TODO: copy data

                //stop old application
                if (!stopApplication(applicationUpgradeRequest.getApplicationUrl(), applicationUpgradeRequest.getContainerId())) {
                    return new AsyncResult<>(new FogOperationResult(containerInfo.getId(), false, environmentInfoService.getFogBaseUrl(), "Failed to stop container"));
                }

                //start new application, delete old container
                boolean startNewAppSuccessful = dockerService.startContainer(fogOperationResult.getContainerId());
                if (!finalizeReplaceContainerOperation(startNewAppSuccessful, applicationUpgradeRequest.getContainerId())) {
                    return new AsyncResult<>(new FogOperationResult(applicationUpgradeRequest.getContainerId(), false, environmentInfoService.getFogBaseUrl(), "upgrade failed, recovered"));
                }
            }
        }
        return new AsyncResult<>(new FogOperationResult(null /*todo*/, true, environmentInfoService.getFogBaseUrl()));
    }
}
