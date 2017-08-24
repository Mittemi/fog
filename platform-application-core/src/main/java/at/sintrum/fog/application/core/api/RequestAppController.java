package at.sintrum.fog.application.core.api;

import at.sintrum.fog.application.core.service.TravelingCoordinationService;
import at.sintrum.fog.core.dto.FogIdentification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Michael Mittermayr on 17.07.2017.
 */
@RestController
public class RequestAppController implements RequestAppApi {

    private final TravelingCoordinationService travelingCoordinationService;

    private static final Logger LOG = LoggerFactory.getLogger(RequestAppController.class);

    public RequestAppController(TravelingCoordinationService travelingCoordinationService) {
        this.travelingCoordinationService = travelingCoordinationService;
    }

    @Override
    public boolean requestApplication(@RequestBody FogIdentification fogIdentification) {

        return travelingCoordinationService.requestMove(fogIdentification);
    }

    @Override
    public List<FogIdentification> getTravelQueue() {
        return travelingCoordinationService.getTargets();
    }
}
