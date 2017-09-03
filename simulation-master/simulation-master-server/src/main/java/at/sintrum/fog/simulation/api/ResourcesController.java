package at.sintrum.fog.simulation.api;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.simulation.scenario.dto.FogResourceInfoDto;
import at.sintrum.fog.simulation.service.FogResourceService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Michael Mittermayr on 21.08.2017.
 */
@RestController
public class ResourcesController implements FogResourcesApi {

    private final FogResourceService fogResourceService;

    public ResourcesController(FogResourceService fogResourceService) {
        this.fogResourceService = fogResourceService;
    }

    @Override
    public FogResourceInfoDto availableResources(@RequestBody FogIdentification fogIdentification) {
        return new FogResourceInfoDto(fogIdentification, fogResourceService.getAvailableResources(fogIdentification));
    }
}
