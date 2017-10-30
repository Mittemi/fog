package at.sintrum.fog.application.service;

import at.sintrum.fog.application.core.service.TravelingCoordinationService;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Created by Michael Mittermayr on 30.10.2017.
 */
@Service
@ConditionalOnProperty(prefix = "fog.app", name = "enable-autocomplete-work-mode", havingValue = "true", matchIfMissing = false)
public class AutocompleteWorkService {

    private final WorkService workService;
    private final TravelingCoordinationService travelingCoordinationService;

    private final Logger LOG = LoggerFactory.getLogger(AutocompleteWorkService.class);

    private DateTime startTime;
    private boolean finished = false;

    public AutocompleteWorkService(WorkService workService, TravelingCoordinationService travelingCoordinationService) {
        this.workService = workService;
        this.travelingCoordinationService = travelingCoordinationService;
        LOG.debug("Enable autocomplete work mode");
    }

    @Scheduled(fixedDelay = 1000)
    private void scheduled() {
        if (finished) {
            LOG.debug("Work already finished");
            return;
        }

        if (startTime == null) {
            startTime = new DateTime();
        }

        int estimatedWorkingTime = travelingCoordinationService.getEstimatedWorkingTime();

        if (Seconds.secondsBetween(startTime, new DateTime()).isGreaterThan(Seconds.seconds(estimatedWorkingTime))) {
            LOG.debug("Autocomplete work service triggers work");
            finished = true;
            workService.doWork();
        }
    }
}
