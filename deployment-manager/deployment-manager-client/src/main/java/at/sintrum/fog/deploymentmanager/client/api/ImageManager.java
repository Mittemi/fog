package at.sintrum.fog.deploymentmanager.client.api;

import at.sintrum.fog.deploymentmanager.api.ImageManagerApi;
import at.sintrum.fog.deploymentmanager.client.Constants;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * Created by Michael Mittermayr on 20.05.2017.
 */
@FeignClient(Constants.APPLICATION_NAME)
public interface ImageManager extends ImageManagerApi {
}
