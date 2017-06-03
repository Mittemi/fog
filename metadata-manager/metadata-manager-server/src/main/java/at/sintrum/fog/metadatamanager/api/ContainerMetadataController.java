package at.sintrum.fog.metadatamanager.api;

import at.sintrum.fog.metadatamanager.api.dto.DockerContainerMetadata;
import at.sintrum.fog.metadatamanager.service.ContainerMetadataService;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Michael Mittermayr on 03.06.2017.
 */
@RestController
public class ContainerMetadataController extends MetadataControllerBase<DockerContainerMetadata> implements ContainerMetadataApi {

    public ContainerMetadataController(ContainerMetadataService dockerContainerMetadataMetadataService) {
        super(dockerContainerMetadataMetadataService);
    }
}
