package at.sintrum.fog.deploymentmanager.client.api;

import at.sintrum.fog.deploymentmanager.api.ContainerManagerApi;
import at.sintrum.fog.deploymentmanager.client.Constants;
import org.springframework.cloud.netflix.feign.FeignClient;


/**
 * Created by Michael Mittermayr on 17.05.2017.
 */
@FeignClient(Constants.APPLICATION_NAME)
public interface ContainerManager extends ContainerManagerApi {
}
