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

    @RequestMapping(value = "getById/{fogId}/{id}/", method = RequestMethod.GET)
    DockerContainerMetadata getById(@PathVariable("fogId") String fogId, @PathVariable("id") String id);

    @RequestMapping(value = "{fogId}/{id}", method = RequestMethod.DELETE)
    void delete(@PathVariable("fogId") String fogId, @PathVariable("id") String id);

    @RequestMapping(value = "{fogId}", method = RequestMethod.GET)
    List<DockerContainerMetadata> getAll(@PathVariable("fogId") String fogId);

    @RequestMapping(value = "reset", method = RequestMethod.POST)
    void reset();

    @RequestMapping(value = "getByInstanceId/{instanceId}", method = RequestMethod.GET)
    List<DockerContainerMetadata> getByInstanceId(@PathVariable("instanceId") String instanceId);

    @RequestMapping(value = "getLatestByInstanceId/{instanceId}", method = RequestMethod.GET)
    DockerContainerMetadata getLatestByInstanceId(@PathVariable("instanceId") String instanceId);
}
