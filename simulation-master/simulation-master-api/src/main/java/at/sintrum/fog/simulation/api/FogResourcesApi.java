package at.sintrum.fog.simulation.api;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.simulation.scenario.dto.FogResourceInfoDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Michael Mittermayr on 21.08.2017.
 */
@RequestMapping("simulation/resources")
public interface FogResourcesApi {

    @RequestMapping(value = "available", method = RequestMethod.POST)
    FogResourceInfoDto availableResources(FogIdentification fogIdentification);
}
