package at.sintrum.fog.deploymentmanager.api;

import at.sintrum.fog.core.service.EnvironmentInfoService;
import at.sintrum.fog.deploymentmanager.api.dto.ApplicationMoveRequest;
import at.sintrum.fog.deploymentmanager.api.dto.ApplicationStartRequest;
import at.sintrum.fog.deploymentmanager.api.dto.ApplicationUpgradeRequest;
import at.sintrum.fog.deploymentmanager.api.dto.FogOperationResult;
import at.sintrum.fog.deploymentmanager.service.ApplicationManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

/**
 * Created by Michael Mittermayr on 31.05.2017.
 */
@RestController
public class ApplicationManager implements ApplicationManagerApi {

    private final Logger LOG = LoggerFactory.getLogger(ApplicationManager.class);

    private final ApplicationManagerService applicationManagerService;
    private final EnvironmentInfoService environmentInfoService;

    public ApplicationManager(ApplicationManagerService applicationManagerService, EnvironmentInfoService environmentInfoService) {

        this.applicationManagerService = applicationManagerService;
        this.environmentInfoService = environmentInfoService;
    }

    //TODO: prevent concurrent start/move/upgrade calls for the same application

    @Override
    public FogOperationResult requestApplicationStart(@RequestBody ApplicationStartRequest applicationStartRequest) {
        try {
            return applicationManagerService.start(applicationStartRequest).get();
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("start failed", e);
        }
        return new FogOperationResult(null, false, environmentInfoService.getFogBaseUrl());
    }

    @Override
    public FogOperationResult moveApplication(@RequestBody ApplicationMoveRequest applicationMoveRequest) {
        try {
            return applicationManagerService.move(applicationMoveRequest).get();
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("move failed", e);
        }
        return new FogOperationResult(null, false, environmentInfoService.getFogBaseUrl());
    }

    @Override
    public FogOperationResult upgradeApplication(@RequestBody ApplicationUpgradeRequest applicationUpgradeRequest) {
        try {
            return applicationManagerService.upgrade(applicationUpgradeRequest).get();
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("upgrade failed", e);
        }
        return new FogOperationResult(null, false, environmentInfoService.getFogBaseUrl());
    }
}
