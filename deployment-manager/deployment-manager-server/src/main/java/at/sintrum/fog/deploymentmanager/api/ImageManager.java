package at.sintrum.fog.deploymentmanager.api;

import at.sintrum.fog.deploymentmanager.api.dto.ImageInfo;
import at.sintrum.fog.deploymentmanager.api.dto.PullImageRequest;
import at.sintrum.fog.deploymentmanager.api.dto.PushImageRequest;
import at.sintrum.fog.deploymentmanager.service.DockerService;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Michael Mittermayr on 20.05.2017.
 */
@RestController
public class ImageManager implements ImageManagerApi {

    private DockerService dockerService;

    public ImageManager(DockerService dockerService) {
        this.dockerService = dockerService;
    }

    @Override
    public List<ImageInfo> getImages() {
        return dockerService.getImages();
    }

    @Override
    public void pullImage(PullImageRequest pullImageRequest) {
        dockerService.pullImage(pullImageRequest);
    }

    @Override
    public void pushImage(PushImageRequest pushImageRequest) {
        dockerService.pushImage(pushImageRequest);
    }
}
