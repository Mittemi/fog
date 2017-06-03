package at.sintrum.fog.metadatamanager.api;

import at.sintrum.fog.metadatamanager.api.dto.DockerImageMetadata;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Michael Mittermayr on 30.05.2017.
 */
@RequestMapping(value = "image")
public interface ImageMetadataApi extends MetadataApi<DockerImageMetadata> {

}
