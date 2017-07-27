package at.sintrum.fog.deploymentmanager.service;

import at.sintrum.fog.core.service.EnvironmentInfoService;
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
import java.util.Optional;

/**
 * Created by Michael Mittermayr on 02.06.2017.
 */
@Service
public class DeploymentService {

    public static final String SERVICE_PROFILE = "SERVICE_PROFILE";
    public static final String EUREKA_SERVICE_URL = "EUREKA_SERVICE_URL";
    public static final String EUREKA_CLIENT_IP = "EUREKA_CLIENT_IP";
    public static final String FOG_BASE_URL = "FOG_BASE_URL";
    public static final String METADATA_ID = "METADATA_ID";
    private Logger LOG = LoggerFactory.getLogger(DeploymentService.class);

    private final EnvironmentInfoService environmentInfoService;
    private DeploymentManagerConfigProperties deploymentManagerConfigProperties;

    public DeploymentService(EnvironmentInfoService environmentInfoService, DeploymentManagerConfigProperties deploymentManagerConfigProperties) {
        this.environmentInfoService = environmentInfoService;
        this.deploymentManagerConfigProperties = deploymentManagerConfigProperties;
    }

    public void enableServiceProfile(CreateContainerRequest createContainerRequest, String profile) {
        Optional<String> first = createContainerRequest.getEnvironment().stream().filter(x -> x.startsWith(SERVICE_PROFILE)).findFirst();

        if (first.isPresent()) {
            String originalString = first.get();
            createContainerRequest.getEnvironment().remove(originalString);
            String profileStr = originalString.substring(SERVICE_PROFILE.length() + 1);

            if (!StringUtils.isEmpty(profileStr)) {
                profileStr = profileStr + "," + profile;
            } else {
                profileStr = profile;
            }
            enableServiceProfile(createContainerRequest, profileStr);
        } else {
            LOG.debug("Set SERVICE_PROFILE to: " + profile);
            addDynamicEnvironmentKey(createContainerRequest.getEnvironment(), SERVICE_PROFILE, profile);
        }
    }

    public CreateContainerRequest buildCreateContainerRequest(DockerImageMetadata imageMetadata) {
        CreateContainerRequest createContainerRequest = new CreateContainerRequest();

        setEnvironment(imageMetadata, createContainerRequest);
        enableServiceProfile(createContainerRequest, environmentInfoService.getServiceProfile());
        setImage(imageMetadata, createContainerRequest);
        setPortInfos(imageMetadata, createContainerRequest);
        return createContainerRequest;
    }


    private void setPortInfos(DockerImageMetadata imageMetadata, CreateContainerRequest createContainerRequest) {
        if (imageMetadata.getPorts() != null) {
            for (Integer port : imageMetadata.getPorts()) {
                addPortMapping(createContainerRequest, port, port);
            }
        }
    }

    public void addPortMapping(CreateContainerRequest createContainerRequest, int containerPort, int hostPort) {
        if (createContainerRequest.getPortInfos() == null) {
            createContainerRequest.setPortInfos(new LinkedList<>());
        }
        createContainerRequest.getPortInfos().add(new PortInfo(containerPort, hostPort));
    }

    private void setEnvironment(DockerImageMetadata imageMetadata, CreateContainerRequest createContainerRequest) {

        List<String> environment = new LinkedList<>();

        if (imageMetadata.getEnvironment() != null) {
            environment.addAll(imageMetadata.getEnvironment());
        }

        if (imageMetadata.isEurekaEnabled()) {
            addDynamicEnvironmentKey(environment, EUREKA_SERVICE_URL, environmentInfoService.getEurekaServiceUrl());
            addDynamicEnvironmentKey(environment, EUREKA_CLIENT_IP, environmentInfoService.getEurekaClientIp());
        }
        addDynamicEnvironmentKey(environment, FOG_BASE_URL, environmentInfoService.getFogBaseUrl());
        addDynamicEnvironmentKey(environment, METADATA_ID, imageMetadata.getId());

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
