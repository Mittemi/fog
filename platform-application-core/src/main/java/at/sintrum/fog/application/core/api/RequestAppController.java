package at.sintrum.fog.application.core.api;

import at.sintrum.fog.application.core.service.TravelingCoordinationService;
import at.sintrum.fog.core.dto.FogIdentification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Michael Mittermayr on 17.07.2017.
 */
@RestController
@RequestMapping(value = "request")
public class RequestAppController {

    private final TravelingCoordinationService travelingCoordinationService;

    private static final Logger LOG = LoggerFactory.getLogger(RequestAppController.class);

    public RequestAppController(TravelingCoordinationService travelingCoordinationService) {
        this.travelingCoordinationService = travelingCoordinationService;
    }

    @RequestMapping(value = "move", method = RequestMethod.POST)
    public boolean requestApplication(@RequestBody FogIdentification fogIdentification) {

        return travelingCoordinationService.requestMove(fogIdentification);
    }
}
