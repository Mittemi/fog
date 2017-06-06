package at.sintrum.fog.metadatamanager.api;

import at.sintrum.fog.metadatamanager.api.dto.DockerContainerMetadata;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by Michael Mittermayr on 03.06.2017.
 */
@RequestMapping(value = "container")
public interface ContainerMetadataApi {
    @RequestMapping(value = "", method = RequestMethod.PUT)
    DockerContainerMetadata store(@RequestBody DockerContainerMetadata metadata);

    @RequestMapping(value = "getById/{id}", method = RequestMethod.GET)
    DockerContainerMetadata getById(@PathVariable("id") String id);

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    void delete(@PathVariable("id") String id);

    @RequestMapping(value = "", method = RequestMethod.GET)
    List<DockerContainerMetadata> getAll();
}
