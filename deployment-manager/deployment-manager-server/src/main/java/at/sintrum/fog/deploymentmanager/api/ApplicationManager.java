package at.sintrum.fog.deploymentmanager.api;

import at.sintrum.fog.deploymentmanager.api.dto.ApplicationMoveRequest;
import at.sintrum.fog.deploymentmanager.api.dto.ApplicationStartRequest;
import at.sintrum.fog.deploymentmanager.api.dto.FogOperationResult;
import at.sintrum.fog.deploymentmanager.service.ApplicationManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Michael Mittermayr on 31.05.2017.
 */
@RestController
public class ApplicationManager implements ApplicationManagerApi {

    private final Logger LOG = LoggerFactory.getLogger(ApplicationManager.class);

    private final ApplicationManagerService applicationManagerService;

    public ApplicationManager(ApplicationManagerService applicationManagerService) {

        this.applicationManagerService = applicationManagerService;
    }


    // @Async
    @Override
    public FogOperationResult requestApplicationStart(@RequestBody ApplicationStartRequest applicationStartRequest) {
        return applicationManagerService.start(applicationStartRequest);
    }

    //   @Async
    @Override
    public FogOperationResult moveApplication(@RequestBody ApplicationMoveRequest applicationMoveRequest) {
        return applicationManagerService.move(applicationMoveRequest);
    }
}
