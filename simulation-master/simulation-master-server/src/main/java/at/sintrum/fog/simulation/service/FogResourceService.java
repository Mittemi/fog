package at.sintrum.fog.simulation.service;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.core.dto.ResourceInfo;

/**
 * Created by Michael Mittermayr on 21.08.2017.
 */
public interface FogResourceService {

    ResourceInfo getUsedResources(FogIdentification fogIdentification);

    ResourceInfo getAvailableResources(FogIdentification fogIdentification);
}
