package at.sintrum.fog.metadatamanager.api;

import at.sintrum.fog.metadatamanager.api.dto.DockerImageMetadata;
import at.sintrum.fog.metadatamanager.service.ApplicationMetadataService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
        applicationMetadataService.storeMetdata(dockerImageMetadata);
    }

    @Override
    public DockerImageMetadata getImageMetadata(@PathVariable("id") String id) {
        return applicationMetadataService.getMetadata(id);
    }
}
