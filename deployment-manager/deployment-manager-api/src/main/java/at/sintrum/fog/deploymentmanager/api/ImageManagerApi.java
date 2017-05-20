package at.sintrum.fog.deploymentmanager.api;

import at.sintrum.fog.deploymentmanager.api.dto.ImageInfo;
import at.sintrum.fog.deploymentmanager.api.dto.PullImageRequest;
import at.sintrum.fog.deploymentmanager.api.dto.PushImageRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by Michael Mittermayr on 20.05.2017.
 */
@RequestMapping(value = "image/")
public interface ImageManagerApi {
    @RequestMapping(value = "", method = RequestMethod.GET)
    List<ImageInfo> getImages();

    @RequestMapping(value = "pull", method = RequestMethod.POST)
    void pullImage(@RequestBody PullImageRequest pullImageRequest);

    @RequestMapping(value = "push", method = RequestMethod.POST)
    void pushImage(@RequestBody PushImageRequest pushImageRequest);
}
