package at.sintrum.fog.simulation.scenario.dto;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.core.dto.ResourceInfo;

/**
 * Created by Michael Mittermayr on 21.08.2017.
 */
public class FogResourceInfoDto {

    private FogIdentification fog;

    private ResourceInfo resourceInfo;

    public FogResourceInfoDto(FogIdentification fog, ResourceInfo resourceInfo) {
        this.fog = fog;
        this.resourceInfo = resourceInfo;
    }

    public FogResourceInfoDto() {
    }

    public FogIdentification getFog() {
        return fog;
    }

    public void setFog(FogIdentification fog) {
        this.fog = fog;
    }

    public ResourceInfo getResourceInfo() {
        return resourceInfo;
    }

    public void setResourceInfo(ResourceInfo resourceInfo) {
        this.resourceInfo = resourceInfo;
    }
}
