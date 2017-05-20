package at.sintrum.fog.deploymentmanager.api;

import at.sintrum.fog.deploymentmanager.api.dto.ContainerInfo;
import at.sintrum.fog.deploymentmanager.api.dto.CreateContainerRequest;
import at.sintrum.fog.deploymentmanager.api.dto.CreateContainerResult;
import at.sintrum.fog.deploymentmanager.service.DockerService;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Michael Mittermayr on 17.05.2017.
 */
@RestController
public class ContainerManager implements ContainerManagerApi {

    private final DockerService dockerService;

    public ContainerManager(DockerService dockerService) {
        this.dockerService = dockerService;
    }


    @Override
    public List<ContainerInfo> getRunningContainers() {
        return dockerService.getContainers().stream().filter(ContainerInfo::isRunning).collect(Collectors.toList());
    }

    @Override
    public List<ContainerInfo> getContainers() {
        return dockerService.getContainers();
    }

    @Override
    public boolean startContainer(String id) {
        return dockerService.startContainer(id);
    }

    @Override
    public boolean stopContainer(String id) {
        return dockerService.stopContainer(id);
    }

    @Override
    public CreateContainerResult createContainer(CreateContainerRequest createContainerRequest) {
        return dockerService.createContainer(createContainerRequest);
    }
}
