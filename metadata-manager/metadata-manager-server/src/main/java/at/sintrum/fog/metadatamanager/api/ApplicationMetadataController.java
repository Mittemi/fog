package at.sintrum.fog.metadatamanager.api;

import at.sintrum.fog.metadatamanager.api.dto.DockerImageMetadata;
import at.sintrum.fog.metadatamanager.api.dto.DockerImageMetadataRequest;
import at.sintrum.fog.metadatamanager.service.ApplicationMetadataService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Michael Mittermayr on 30.05.2017.
 */
@RestController
public class ApplicationMetadataController implements ApplicationMetadataApi {

    private ApplicationMetadataService applicationMetadataService;

    public ApplicationMetadataController(ApplicationMetadataService applicationMetadataService) {
        this.applicationMetadataService = applicationMetadataService;
    }

    @Override
    public void storeImageMetadata(@RequestBody DockerImageMetadata dockerImageMetadata) {
        applicationMetadataService.storeMetadata(dockerImageMetadata);
    }

    @Override
    public DockerImageMetadata getImageMetadata(@RequestBody DockerImageMetadataRequest request) {
        DockerImageMetadata metadata = applicationMetadataService.getMetadata(request.getImageId());

//        if (metadata == null) {
//            throw new RuntimeException();
//        }
        return metadata;
    }

    @Override
    public List<DockerImageMetadata> getAll() {
        return applicationMetadataService.getAll();
    }
}
