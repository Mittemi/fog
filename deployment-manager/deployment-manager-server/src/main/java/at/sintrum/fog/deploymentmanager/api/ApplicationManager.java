package at.sintrum.fog.deploymentmanager.api;

import at.sintrum.fog.deploymentmanager.api.dto.*;
import at.sintrum.fog.deploymentmanager.config.DeploymentManagerConfigProperties;
import at.sintrum.fog.deploymentmanager.service.DockerService;
import at.sintrum.fog.deploymentmanager.service.FogEnvironmentService;
import at.sintrum.fog.metadatamanager.api.dto.DockerImageMetadata;
import at.sintrum.fog.metadatamanager.api.dto.DockerImageMetadataRequest;
import at.sintrum.fog.metadatamanager.client.api.ApplicationMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Mittermayr on 31.05.2017.
 */
@RestController
public class ApplicationManager implements ApplicationManagerApi {

    private final Logger LOG = LoggerFactory.getLogger(ApplicationManager.class);

    private final DockerService dockerService;
    private ApplicationMetadata applicationMetadata;
    private DeploymentManagerConfigProperties deploymentManagerConfigProperties;
    private FogEnvironmentService fogEnvironmentService;

    //TODO: move some stuff to a service


    public ApplicationManager(DockerService dockerService, ApplicationMetadata applicationMetadata, DeploymentManagerConfigProperties deploymentManagerConfigProperties, FogEnvironmentService fogEnvironmentService) {
        this.dockerService = dockerService;
        this.applicationMetadata = applicationMetadata;
        this.deploymentManagerConfigProperties = deploymentManagerConfigProperties;
        this.fogEnvironmentService = fogEnvironmentService;
    }


    @Override
    public void requestApplicationStart(@RequestBody ApplicationStartRequest applicationStartRequest) {

        String imageId = applicationStartRequest.getImageId();

        LOG.info("Request application start: " + imageId);

        DockerImageMetadata imageMetadata = applicationMetadata.getImageMetadata(new DockerImageMetadataRequest(imageId));

        if (imageMetadata == null) {
            LOG.error("Image metadata missing for: " + imageId);
        } else {
            dockerService.pullImage(new PullImageRequest(imageMetadata.getId(), imageMetadata.getTag()));

            CreateContainerRequest createContainerRequest = new CreateContainerRequest();

            setEnvironment(imageMetadata, createContainerRequest);
            setImage(imageMetadata, createContainerRequest);
            setPortInfos(imageMetadata, createContainerRequest);

            CreateContainerResult container = dockerService.createContainer(createContainerRequest);

            //TODO: logging
            dockerService.startContainer(container.getId());
        }
    }

    private void setPortInfos(DockerImageMetadata imageMetadata, CreateContainerRequest createContainerRequest) {
        List<PortInfo> portInfos = new LinkedList<>();
        if (imageMetadata.getPorts() != null) {
            for (Integer port : imageMetadata.getPorts()) {
                portInfos.add(new PortInfo(port, port));
            }
        }
        createContainerRequest.setPortInfos(portInfos);
    }

    private void setEnvironment(DockerImageMetadata imageMetadata, CreateContainerRequest createContainerRequest) {

        List<String> environment = new LinkedList<>();

        if (imageMetadata.getEnvironment() != null) {
            environment.addAll(imageMetadata.getEnvironment());
        }

        addDynamicEnvironmentKey(environment, "EUREKA_SERVICE_URL", fogEnvironmentService.getEurekaServiceUrl());
        addDynamicEnvironmentKey(environment, "EUREKA_CLIENT_IP", fogEnvironmentService.getEurekaClientIp());
        addDynamicEnvironmentKey(environment, "FOG_BASE_URL", fogEnvironmentService.getFogBaseUrl());

        createContainerRequest.setEnvironment(environment);
    }

    private void addDynamicEnvironmentKey(List<String> environment, String key, String value) {
        if (!environment.stream().anyMatch(x -> x.startsWith(key))) {
            LOG.info("Set dynamic env key '" + key + "' to value: " + value);
            environment.add(key + "=" + value);
        } else {
            LOG.info("Skip dynamic env key: " + key);
        }
    }

    private void setImage(DockerImageMetadata imageMetadata, CreateContainerRequest createContainerRequest) {

        String prefix = deploymentManagerConfigProperties.getRegistry() + "/";

        if (!StringUtils.isEmpty(imageMetadata.getTag())) {
            createContainerRequest.setImage(prefix + imageMetadata.getId() + ":" + imageMetadata.getTag());
        } else {
            createContainerRequest.setImage(prefix + imageMetadata.getId());
        }
    }
}
