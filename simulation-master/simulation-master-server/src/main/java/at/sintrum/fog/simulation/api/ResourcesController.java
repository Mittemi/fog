package at.sintrum.fog.simulation.api;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.core.dto.ResourceInfo;
import at.sintrum.fog.simulation.api.dto.FogResourceInfoDto;
import at.sintrum.fog.simulation.service.FogResourceService;
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
    public FogResourceInfoDto availableResources(FogIdentification fogIdentification) {
        return new FogResourceInfoDto(fogIdentification, fogResourceService.getAvailableResources(fogIdentification));
    }

    @Override
    public FogResourceInfoDto usedResources(FogIdentification fogIdentification) {
        return new FogResourceInfoDto(fogIdentification, fogResourceService.getUsedResources(fogIdentification));
    }

    @Override
    public FogResourceInfoDto freeResources(FogIdentification fogIdentification) {
        ResourceInfo free = fogResourceService.getAvailableResources(fogIdentification).subtract(fogResourceService.getUsedResources(fogIdentification));
        return new FogResourceInfoDto(fogIdentification, free);
    }
}
