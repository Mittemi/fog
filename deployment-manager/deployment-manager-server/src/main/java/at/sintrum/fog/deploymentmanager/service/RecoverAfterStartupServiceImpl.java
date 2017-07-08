package at.sintrum.fog.deploymentmanager.service;

import at.sintrum.fog.deploymentmanager.api.dto.ApplicationMoveRequest;
import at.sintrum.fog.deploymentmanager.api.dto.ApplicationStartRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

/**
 * Created by Michael Mittermayr on 08.07.2017.
 */
//@Service
public class RecoverAfterStartupServiceImpl {

    private final ApplicationManagerService applicationManagerService;
    private final MetadataServiceImpl metadataService;

    private final Logger LOG = LoggerFactory.getLogger(RecoverAfterStartupServiceImpl.class);

    public RecoverAfterStartupServiceImpl(ApplicationManagerService applicationManagerService, MetadataServiceImpl metadataService) {
        this.applicationManagerService = applicationManagerService;
        this.metadataService = metadataService;
    }

    @PostConstruct
    public void recover() {
        LOG.info("Start on startup recovery.");
        for (ApplicationMoveRequest applicationMoveRequest : metadataService.getUnfinishedMoveRequests()) {
            LOG.info("Recover app move for container: " + applicationMoveRequest.getContainerId());
            applicationManagerService.move(applicationMoveRequest);
        }

        for (ApplicationStartRequest applicationStartRequest : metadataService.getUnfinishedStartupRequests()) {
            LOG.info("Recover app start for: " + applicationStartRequest.getMetadataId());
            applicationManagerService.start(applicationStartRequest);
        }
        LOG.info("On Startup recover finished");
    }
}
