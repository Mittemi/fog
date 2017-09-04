package at.sintrum.fog.deploymentmanager.api;

import at.sintrum.fog.deploymentmanager.api.dto.ImageInfo;
import at.sintrum.fog.deploymentmanager.api.dto.PullImageRequest;
import at.sintrum.fog.deploymentmanager.api.dto.PushImageRequest;
import at.sintrum.fog.deploymentmanager.api.dto.TagImageRequest;
import at.sintrum.fog.deploymentmanager.service.DockerService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Michael Mittermayr on 20.05.2017.
 */
@RestController
public class ImageManagerController implements ImageManagerApi {

    private DockerService dockerService;

    public ImageManagerController(DockerService dockerService) {
        this.dockerService = dockerService;
    }

    @Override
    public List<ImageInfo> getImages() {
        return dockerService.getImages();
    }

    @Override
    public void pullImage(@RequestBody PullImageRequest pullImageRequest) {
        dockerService.pullImage(pullImageRequest);
    }

    @Override
    public void pushImage(@RequestBody PushImageRequest pushImageRequest) {
        dockerService.pushImage(pushImageRequest);
    }

    @Override
    public void tagImage(TagImageRequest tagImageRequest) {
        dockerService.tagImage(tagImageRequest.getId(), tagImageRequest.getRepository(), tagImageRequest.getTag());
    }
}
