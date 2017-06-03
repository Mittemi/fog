package at.sintrum.fog.metadatamanager.api;

import at.sintrum.fog.metadatamanager.api.dto.DockerImageMetadata;
import at.sintrum.fog.metadatamanager.service.ImageMetadataService;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Michael Mittermayr on 30.05.2017.
 */
@RestController
public class ImageMetadataController extends MetadataControllerBase<DockerImageMetadata> implements ImageMetadataApi {

    public ImageMetadataController(ImageMetadataService dockerImageMetadataMetadataService) {
        super(dockerImageMetadataMetadataService);
    }
}
