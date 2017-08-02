package at.sintrum.fog.metadatamanager.api;

import at.sintrum.fog.metadatamanager.api.dto.DockerContainerMetadata;
import at.sintrum.fog.metadatamanager.service.ContainerMetadataService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Michael Mittermayr on 03.06.2017.
 */
@RestController
public class ContainerMetadataController implements ContainerMetadataApi {

    private final ContainerMetadataService metadataService;

    public ContainerMetadataController(ContainerMetadataService dockerContainerMetadataMetadataService) {
        metadataService = dockerContainerMetadataMetadataService;
    }

    @Override
    public List<DockerContainerMetadata> getAll(@PathVariable("fogId") String fogId) {
        return metadataService.getAll(fogId);
    }

    @Override
    public DockerContainerMetadata store(@RequestBody DockerContainerMetadata metadata) {
        return metadataService.store(metadata);
    }

    @Override
    public DockerContainerMetadata getById(@PathVariable("fogId") String fogId, @PathVariable("id") String id) {
        return metadataService.get(fogId, id);
    }

    @Override
    public void delete(@PathVariable("fogId") String fogId, @PathVariable("id") String id) {
        metadataService.delete(fogId, id);
    }

    @Override
    public void reset() {
        metadataService.deleteAll();
    }

    @Override
    public List<DockerContainerMetadata> getByInstanceId(@PathVariable("instanceId") String instanceId) {
        return metadataService.getByInstance(instanceId);
    }
}
