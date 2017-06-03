package at.sintrum.fog.metadatamanager.api;

import at.sintrum.fog.metadatamanager.api.dto.DockerContainerMetadata;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Michael Mittermayr on 03.06.2017.
 */
@RequestMapping(value = "container")
public interface ContainerMetadataApi extends MetadataApi<DockerContainerMetadata> {

}
