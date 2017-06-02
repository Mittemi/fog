package at.sintrum.fog.metadatamanager.api;

import at.sintrum.fog.metadatamanager.api.dto.DockerImageMetadata;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by Michael Mittermayr on 30.05.2017.
 */
@RequestMapping(value = "application")
public interface ApplicationMetadataApi {

    @RequestMapping(value = "", method = RequestMethod.PUT)
    DockerImageMetadata storeImageMetadata(@RequestBody DockerImageMetadata dockerImageMetadata);

    @RequestMapping(value = "getById/{id}", method = RequestMethod.GET)
    DockerImageMetadata getImageMetadata(@PathVariable("id") String id);

    @RequestMapping(value = "getAll", method = RequestMethod.POST)
    List<DockerImageMetadata> getAll();
}
