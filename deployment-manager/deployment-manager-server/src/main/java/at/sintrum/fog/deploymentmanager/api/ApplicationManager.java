package at.sintrum.fog.deploymentmanager.api;

import at.sintrum.fog.core.service.EnvironmentInfoService;
import at.sintrum.fog.deploymentmanager.api.dto.*;
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
            if (!environmentInfoService.getServiceProfile().contains("cloud")) {
                LOG.error("Upgrade application called in non cloud environment");
                return new FogOperationResult(applicationUpgradeRequest.getContainerId(), false, environmentInfoService.getFogBaseUrl(), "Operation only allowed in clouds.");
            }

            return applicationManagerService.upgrade(applicationUpgradeRequest).get();
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("upgrade failed", e);
        }
        return new FogOperationResult(null, false, environmentInfoService.getFogBaseUrl());
    }

    @Override
    public FogOperationResult recoverApplication(@RequestBody ApplicationRecoveryRequest applicationRecoveryRequest) {
        try {
            return applicationManagerService.recover(applicationRecoveryRequest).get();
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("recover failed", e);
        }
        return new FogOperationResult(null, false, environmentInfoService.getFogBaseUrl());
    }

    @Override
    public FogOperationResult removeApplication(@RequestBody ApplicationRemoveRequest applicationRemoveRequest) {
        try {
            return applicationManagerService.remove(applicationRemoveRequest).get();
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("remove failed", e);
        }
        return new FogOperationResult(null, false, environmentInfoService.getFogBaseUrl());
    }
}
