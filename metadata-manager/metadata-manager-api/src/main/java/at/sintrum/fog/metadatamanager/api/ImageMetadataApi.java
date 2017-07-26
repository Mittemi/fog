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
@RequestMapping(value = "image")
public interface ImageMetadataApi {

    @RequestMapping(value = "", method = RequestMethod.PUT)
    DockerImageMetadata store(@RequestBody DockerImageMetadata metadata);

    @RequestMapping(value = "checkpoint/{id}/{tag}", method = RequestMethod.PUT)
    DockerImageMetadata createCheckpoint(@PathVariable("id") String id, @PathVariable("tag") String tag);

    @RequestMapping(value = "getById/{id}", method = RequestMethod.GET)
    DockerImageMetadata getById(@PathVariable("id") String id);

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    void delete(@PathVariable("id") String id);

    @RequestMapping(value = "", method = RequestMethod.GET)
    List<DockerImageMetadata> getAll();
}
