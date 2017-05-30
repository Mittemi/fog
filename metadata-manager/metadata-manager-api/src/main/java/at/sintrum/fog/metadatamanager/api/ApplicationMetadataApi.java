package at.sintrum.fog.metadatamanager.api;

import at.sintrum.fog.metadatamanager.api.dto.DockerImageMetadata;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Michael Mittermayr on 30.05.2017.
 */
@RequestMapping(value = "application")
public interface ApplicationMetadataApi {

    @RequestMapping(value = "", method = RequestMethod.PUT)
    void storeContainerMetadata(@RequestBody DockerImageMetadata dockerImageMetadata);

    @RequestMapping(value = "{imageId}", method = RequestMethod.GET)
    DockerImageMetadata getContainerMetadata(@PathVariable("imageId") String imageId);
}
