package at.sintrum.fog.deploymentmanager.api;

import at.sintrum.fog.deploymentmanager.api.dto.*;
import at.sintrum.fog.deploymentmanager.service.DockerService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ContainerInfo getContainerInfo(@PathVariable("id") String id) {
        return dockerService.getContainerInfo(id);
    }

    @Override
    public boolean startContainer(@PathVariable("id") String id) {
        return dockerService.startContainer(id);
    }

    @Override
    public boolean stopContainer(@PathVariable("id") String id) {
        return dockerService.stopContainer(id);
    }

    @Override
    public CreateContainerResult createContainer(@RequestBody CreateContainerRequest createContainerRequest) {
        return dockerService.createContainer(createContainerRequest);
    }

    @Override
    public CommitContainerResult commitContainer(@RequestBody CommitContainerRequest commitContainerRequest) {
        return dockerService.commitContainer(commitContainerRequest);
    }
}
