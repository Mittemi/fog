package at.sintrum.fog.simulation.service;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.core.dto.ResourceInfo;

/**
 * Created by Michael Mittermayr on 21.08.2017.
 */
public interface FogResourceService {

    void setResourceRestriction(FogIdentification fogIdentification, ResourceInfo resourceInfo);

    ResourceInfo getAvailableResources(FogIdentification fogIdentification);

    void reset();
}
