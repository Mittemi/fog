package at.sintrum.fog.deploymentmanager.service;

import at.sintrum.fog.deploymentmanager.api.dto.CreateContainerRequest;
import at.sintrum.fog.deploymentmanager.api.dto.PortInfo;
import at.sintrum.fog.deploymentmanager.config.DeploymentManagerConfigProperties;
import at.sintrum.fog.metadatamanager.api.dto.DockerImageMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Mittermayr on 02.06.2017.
 */
@Service
public class DeploymentService {

    private Logger LOG = LoggerFactory.getLogger(DeploymentService.class);
    private FogEnvironmentService fogEnvironmentService;
    private DeploymentManagerConfigProperties deploymentManagerConfigProperties;

    public DeploymentService(FogEnvironmentService fogEnvironmentService, DeploymentManagerConfigProperties deploymentManagerConfigProperties) {
        this.fogEnvironmentService = fogEnvironmentService;
        this.deploymentManagerConfigProperties = deploymentManagerConfigProperties;
    }

    public CreateContainerRequest buildCreateContainerRequest(DockerImageMetadata imageMetadata) {
        CreateContainerRequest createContainerRequest = new CreateContainerRequest();

        setEnvironment(imageMetadata, createContainerRequest);
        setImage(imageMetadata, createContainerRequest);
        setPortInfos(imageMetadata, createContainerRequest);
        return createContainerRequest;
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
        if (environment.stream().noneMatch(x -> x.startsWith(key))) {
            LOG.info("Set dynamic env key '" + key + "' to value: " + value);
            environment.add(key + "=" + value);
        } else {
            LOG.info("Skip dynamic env key: " + key);
        }
    }

    private void setImage(DockerImageMetadata imageMetadata, CreateContainerRequest createContainerRequest) {

        //TODO: use same method as in dockerClient
        String prefix = getRepositoryName(imageMetadata.getImage());

        if (!StringUtils.isEmpty(imageMetadata.getTag())) {
            createContainerRequest.setImage(prefix + ":" + imageMetadata.getTag());
        } else {
            createContainerRequest.setImage(prefix);
        }
    }

    public String getRepositoryName(String imageName) {

        if (!imageName.startsWith(deploymentManagerConfigProperties.getRegistry())) {

            if (imageName.contains("/")) {
                LOG.error("Invalid repository link");
            }
            //TODO: prevent double /
            imageName = deploymentManagerConfigProperties.getRegistry() + "/" + imageName;
        }

        int idx = imageName.lastIndexOf("/");
        if (imageName.lastIndexOf(":") > idx) {
            String old = imageName;
            imageName = imageName.substring(0, imageName.lastIndexOf(":"));
            LOG.debug("Rewrite repository string from '" + old + "' to '" + imageName + "'");
        }

        return imageName;
    }
}
