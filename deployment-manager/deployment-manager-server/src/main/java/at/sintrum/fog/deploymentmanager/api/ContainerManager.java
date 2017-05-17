package at.sintrum.fog.deploymentmanager.api;

import at.sintrum.fog.deploymentmanager.api.dto.ContainerInfo;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Michael Mittermayr on 17.05.2017.
 */
@RestController
public class ContainerManager implements ContainerManagerApi {


    @Override
    public List<ContainerInfo> getRunningContainers() {
        return null;
    }

    @Override
    public List<ContainerInfo> getContainers() {
        return null;
    }
}
