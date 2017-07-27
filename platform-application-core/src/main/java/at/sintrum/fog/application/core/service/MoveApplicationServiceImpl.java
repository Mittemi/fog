package at.sintrum.fog.application.core.service;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.core.service.EnvironmentInfoService;
import at.sintrum.fog.deploymentmanager.api.dto.ApplicationMoveRequest;
import at.sintrum.fog.deploymentmanager.client.api.ApplicationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by Michael Mittermayr on 24.05.2017.
 */
@Service
public class MoveApplicationServiceImpl implements MoveApplicationService {

    private final EnvironmentInfoService environmentInfoService;
    private final ApplicationManager applicationManager;
    private final TravelingCoordinationService travelingCoordinationService;
    private final CloudLocatorService cloudLocatorService;

    private final Logger LOG = LoggerFactory.getLogger(MoveApplicationServiceImpl.class);

    public MoveApplicationServiceImpl(EnvironmentInfoService environmentInfoService, ApplicationManager applicationManager, TravelingCoordinationService travelingCoordinationService, CloudLocatorService cloudLocatorService) {
        this.environmentInfoService = environmentInfoService;
        this.applicationManager = applicationManager;
        this.travelingCoordinationService = travelingCoordinationService;
        this.cloudLocatorService = cloudLocatorService;
    }

    @Override
    public void moveApplication(FogIdentification target) {
        LOG.debug("Request application move to fog: " + target.toUrl());
        applicationManager.moveApplication(new ApplicationMoveRequest(environmentInfoService.getOwnContainerId(), target.toUrl()));
    }

    public void moveAppIfRequired() {
        // we assume work has been finished and we are ready to move somewhere else
        if (travelingCoordinationService.hasNextTarget()) {
            LOG.debug("New application target, let's move");
            FogIdentification nextTarget = travelingCoordinationService.getNextTarget();
            if (nextTarget != null) {
                applicationManager.moveApplication(new ApplicationMoveRequest(environmentInfoService.getOwnContainerId(), nextTarget.toUrl()));
            } else {
                LOG.error("Move target is null. Can't move!");
            }
        } else {
            if (!environmentInfoService.isCloud()) {
                LOG.info("Let's move to the cloud, there is no target in queue right now");
                String cloudBaseUrl = cloudLocatorService.getCloudBaseUrl();
                if (!StringUtils.isEmpty(cloudBaseUrl)) {
                    applicationManager.moveApplication(new ApplicationMoveRequest(environmentInfoService.getOwnContainerId(), cloudBaseUrl));
                } else {
                    LOG.warn("We can't move! Cloud was not found.");
                }
            }
        }
    }
}
