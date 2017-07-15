package at.sintrum.fog.application.core.service;

import at.sintrum.fog.applicationhousing.api.dto.AppIdentification;
import at.sintrum.fog.applicationhousing.api.dto.AppUpdateInfo;
import at.sintrum.fog.applicationhousing.client.api.AppEvolution;
import at.sintrum.fog.core.service.EnvironmentInfoService;
import at.sintrum.fog.deploymentmanager.api.dto.ApplicationUpgradeRequest;
import at.sintrum.fog.deploymentmanager.client.api.ApplicationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Created by Michael Mittermayr on 15.07.2017.
 */
@Service
@Profile("standby")
public class StandbyServiceImpl implements StandbyService {

    private final ApplicationManager applicationManager;
    private final AppEvolution appEvolution;
    private final EnvironmentInfoService environmentInfoService;

    private boolean disableAppServicing;

    private static final Logger LOG = LoggerFactory.getLogger(StandbyServiceImpl.class);

    public StandbyServiceImpl(ApplicationManager applicationManager, AppEvolution appEvolution, EnvironmentInfoService environmentInfoService) {
        this.applicationManager = applicationManager;
        this.appEvolution = appEvolution;
        this.environmentInfoService = environmentInfoService;
        if (!environmentInfoService.isCloud()) {
            LOG.error("This service should not run in non cloud environments");
        }
        LOG.debug("Standby Service has been initialized");
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

    }

    @Scheduled(fixedRate = 30000)
    public void applicationServicing() {

        if (disableAppServicing) {
            LOG.debug("Skip this application servicing call!");
            return;
        }
        disableAppServicing = true;
        LOG.debug("Execute application servicing tasks!");

        upgradeAppIfRequired();

        disableAppServicing = false;
    }

    private void upgradeAppIfRequired() {
        try {
            AppUpdateInfo appUpdateInfo = appEvolution.checkForUpdate(new AppIdentification(environmentInfoService.getMetadataId()));

            if (appUpdateInfo.isUpdateRequired()) {
                LOG.debug("Update is required. Request upgrade!");
                ApplicationUpgradeRequest applicationUpgradeRequest = new ApplicationUpgradeRequest();
                applicationUpgradeRequest.setContainerId(environmentInfoService.getOwnContainerId());
                applicationUpgradeRequest.setApplicationUrl(environmentInfoService.getOwnUrl());
                applicationManager.upgradeApplication(applicationUpgradeRequest);
            }

        } catch (Exception ex) {
            LOG.error("Check for updates failed", ex);
        }
    }
}
