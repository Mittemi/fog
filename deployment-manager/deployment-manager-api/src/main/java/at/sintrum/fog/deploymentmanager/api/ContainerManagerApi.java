package at.sintrum.fog.deploymentmanager.api;

import at.sintrum.fog.deploymentmanager.api.dto.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by Michael Mittermayr on 17.05.2017.
 */
@RequestMapping(value = "container/")
public interface ContainerManagerApi {

    @RequestMapping(value = "running", method = RequestMethod.GET)
    List<ContainerInfo> getRunningContainers();

    @RequestMapping(value = "", method = RequestMethod.GET)
    List<ContainerInfo> getContainers();

    @RequestMapping(value = "{id}/start", method = RequestMethod.POST)
    boolean startContainer(@PathVariable("id") String id);

    @RequestMapping(value = "{id}/stop", method = RequestMethod.POST)
    boolean stopContainer(@PathVariable("id") String id);

    @RequestMapping(value = "create", method = RequestMethod.PUT)
    CreateContainerResult createContainer(@RequestBody CreateContainerRequest createContainerRequest);

    @RequestMapping(value = "commit", method = RequestMethod.POST)
    CommitContainerResult commitContainer(CommitContainerRequest commitContainerRequest);
}
