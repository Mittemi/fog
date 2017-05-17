package at.sintrum.fog.deploymentmanager.api;

import at.sintrum.fog.deploymentmanager.api.dto.ContainerInfo;
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
}
