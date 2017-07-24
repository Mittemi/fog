package at.sintrum.fog.metadatamanager.api;

import at.sintrum.fog.metadatamanager.api.dto.DockerImageMetadata;
import at.sintrum.fog.metadatamanager.service.ImageMetadataService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Michael Mittermayr on 30.05.2017.
 */
@RestController
public class ImageMetadataController implements ImageMetadataApi {

    private final ImageMetadataService metadataService;

    public ImageMetadataController(ImageMetadataService dockerImageMetadataMetadataService) {
        metadataService = dockerImageMetadataMetadataService;
    }

    @Override
    public List<DockerImageMetadata> getAll() {
        return metadataService.getAll();
    }

    @Override
    public DockerImageMetadata store(@RequestBody DockerImageMetadata metadata) {
        return metadataService.store(metadata);
    }

    @Override
    public DockerImageMetadata getById(@PathVariable("id") String id) {
        return metadataService.get(id);
    }

    @Override
    public void delete(@PathVariable("id") String id) {
        metadataService.delete(id);
    }
}
