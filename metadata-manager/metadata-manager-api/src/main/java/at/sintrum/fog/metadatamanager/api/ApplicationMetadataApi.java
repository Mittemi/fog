package at.sintrum.fog.metadatamanager.api;

import at.sintrum.fog.metadatamanager.api.dto.DockerImageMetadata;
import at.sintrum.fog.metadatamanager.api.dto.DockerImageMetadataRequest;
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
    void storeImageMetadata(@RequestBody DockerImageMetadata dockerImageMetadata);

    @RequestMapping(value = "getById", method = RequestMethod.POST)
        //TODO: use GET if possible
    DockerImageMetadata getImageMetadata(@RequestBody DockerImageMetadataRequest request);

    @RequestMapping(value = "getAll", method = RequestMethod.POST)
    List<DockerImageMetadata> getAll();
}
