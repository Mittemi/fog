package at.sintrum.fog.simulation.service;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.core.dto.ResourceInfo;
import org.springframework.stereotype.Service;

/**
 * Created by Michael Mittermayr on 21.08.2017.
 */
@Service
public class FogResourceServiceImpl implements FogResourceService {

    @Override
    public ResourceInfo getUsedResources(FogIdentification fogIdentification) {
        return new ResourceInfo(0, 0, 0, 0);
    }

    @Override
    public ResourceInfo getAvailableResources(FogIdentification fogIdentification) {
        return new ResourceInfo(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
    }
}
