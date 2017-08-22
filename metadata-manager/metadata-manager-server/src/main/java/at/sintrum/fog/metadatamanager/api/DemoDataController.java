package at.sintrum.fog.metadatamanager.api;

import at.sintrum.fog.metadatamanager.api.dto.DockerImageMetadata;
import at.sintrum.fog.metadatamanager.service.ApplicationStateMetadataService;
import at.sintrum.fog.metadatamanager.service.ContainerMetadataService;
import at.sintrum.fog.metadatamanager.service.ImageMetadataService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Michael Mittermayr on 08.07.2017.
 */
@RestController
@RequestMapping(value = "demo")
public class DemoDataController {

    private final ContainerMetadataService containerMetadataService;
    private final ImageMetadataService imageMetadataService;
    private final ApplicationStateMetadataService applicationStateMetadataService;

    public DemoDataController(ContainerMetadataService containerMetadataService, ImageMetadataService imageMetadataService, ApplicationStateMetadataService applicationStateMetadataService) {
        this.containerMetadataService = containerMetadataService;
        this.imageMetadataService = imageMetadataService;
        this.applicationStateMetadataService = applicationStateMetadataService;
    }

    @RequestMapping(value = "reset", method = RequestMethod.POST)
    public List<DockerImageMetadata> reset() {
        containerMetadataService.deleteAll();
        imageMetadataService.deleteAll();
        applicationStateMetadataService.deleteAll();

        return Arrays.asList(
                createImageMetadata("test-application", 10000, true),
                createImageMetadata("test-application", 10000, false),
                createImageMetadata("another-application", 10001, true),
                createImageMetadata("another-application", 10001, false)
        );
    }

    private DockerImageMetadata createImageMetadata(String name, int port, boolean enableDebug) {
        DockerImageMetadata imageMetadata = new DockerImageMetadata();
        imageMetadata.setImage("deb.hw.sintrum.at:5000/test-application");
        imageMetadata.setApplicationName(name);
        imageMetadata.setTag("latest");
        imageMetadata.setEurekaEnabled(true);
        imageMetadata.setEnableDebugging(enableDebug);
        imageMetadata.setEnvironment(Collections.singletonList("SERVER_PORT=" + port));
        imageMetadata.setPorts(Collections.singletonList(port));
        imageMetadata.setAppStorageDirectory("/app/storage/");

        return imageMetadataService.store(imageMetadata);
    }
}
