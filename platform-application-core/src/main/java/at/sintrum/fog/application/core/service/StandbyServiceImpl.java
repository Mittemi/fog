package at.sintrum.fog.application.core.service;

import at.sintrum.fog.core.service.EnvironmentInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Created by Michael Mittermayr on 15.07.2017.
 */
@Service
@Profile("standby")
public class StandbyServiceImpl implements StandbyService {

    private final ApplicationLifecycleService applicationLifecycleService;

    private boolean disableAppServicing;

    private static final Logger LOG = LoggerFactory.getLogger(StandbyServiceImpl.class);

    public StandbyServiceImpl(EnvironmentInfoService environmentInfoService, ApplicationLifecycleService applicationLifecycleService) {
        this.applicationLifecycleService = applicationLifecycleService;
        if (!environmentInfoService.isCloud()) {
            LOG.error("This service should not run in non cloud environments");
        }
        LOG.debug("Standby Service has been initialized");
    }

    @Scheduled(fixedRate = 30000)
    public void applicationServicing() {

        if (disableAppServicing) {
            LOG.debug("Skip this application servicing call!");
            return;
        }
        disableAppServicing = true;
        LOG.debug("Execute application servicing tasks!");

        if (!applicationLifecycleService.upgradeAppIfRequired()) {
            // no upgrade --> check if we should move
            applicationLifecycleService.moveAppIfRequired();
        }
        disableAppServicing = false;
    }
}
