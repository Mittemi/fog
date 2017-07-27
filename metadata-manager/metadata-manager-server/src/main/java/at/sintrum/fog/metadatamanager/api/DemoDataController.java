package at.sintrum.fog.metadatamanager.api;

import at.sintrum.fog.metadatamanager.api.dto.DockerImageMetadata;
import at.sintrum.fog.metadatamanager.service.ContainerMetadataService;
import at.sintrum.fog.metadatamanager.service.ImageMetadataService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

/**
 * Created by Michael Mittermayr on 08.07.2017.
 */
@RestController
@RequestMapping(value = "demo")
public class DemoDataController {

    private final ContainerMetadataService containerMetadataService;
    private final ImageMetadataService imageMetadataService;

    public DemoDataController(ContainerMetadataService containerMetadataService, ImageMetadataService imageMetadataService) {
        this.containerMetadataService = containerMetadataService;
        this.imageMetadataService = imageMetadataService;
    }

    @RequestMapping(value = "reset", method = RequestMethod.POST)
    public DockerImageMetadata reset() {
        containerMetadataService.deleteAll();
        imageMetadataService.deleteAll();

        DockerImageMetadata imageMetadata = new DockerImageMetadata();
        imageMetadata.setImage("deb.hw.sintrum.at:5000/test-application");
        imageMetadata.setTag("latest");
        imageMetadata.setEurekaEnabled(true);
        imageMetadata.setEnableDebugging(true);
        imageMetadata.setEnvironment(Collections.singletonList("SERVER_PORT=10000"));
        imageMetadata.setPorts(Collections.singletonList(10000));
        imageMetadata.setAppStorageDirectory("/app/storage");

        return imageMetadataService.store(imageMetadata);
    }
}
