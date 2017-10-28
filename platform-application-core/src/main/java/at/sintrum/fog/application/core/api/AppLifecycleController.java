package at.sintrum.fog.application.core.api;

import at.sintrum.fog.application.core.service.ApplicationLifecycleService;
import at.sintrum.fog.application.core.service.TravelingCoordinationService;
import at.sintrum.fog.core.dto.FogIdentification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Michael Mittermayr on 17.07.2017.
 */
@RestController
public class AppLifecycleController implements AppLifecycleApi {

    private final TravelingCoordinationService travelingCoordinationService;
    private final ApplicationLifecycleService applicationLifecycleService;

    private static final Logger LOG = LoggerFactory.getLogger(AppLifecycleController.class);

    public AppLifecycleController(TravelingCoordinationService travelingCoordinationService, ApplicationLifecycleService applicationLifecycleService) {
        this.travelingCoordinationService = travelingCoordinationService;
        this.applicationLifecycleService = applicationLifecycleService;
    }

//    @Override
//    public boolean requestApplication(@RequestBody RequestAppDto requestAppDto) {
//
//        return travelingCoordinationService.requestMove(requestAppDto);
//    }

    @Override
    public boolean tearDownApplication() {
        return applicationLifecycleService.tearDown();
    }

    @Override
    public List<FogIdentification> getTravelQueue() {
        return travelingCoordinationService.getTargets();
    }
}
