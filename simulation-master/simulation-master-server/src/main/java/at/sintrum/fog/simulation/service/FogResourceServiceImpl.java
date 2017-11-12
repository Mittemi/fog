package at.sintrum.fog.simulation.service;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.core.dto.ResourceInfo;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Michael Mittermayr on 21.08.2017.
 */
@Service
public class FogResourceServiceImpl implements FogResourceService {

    private final ConcurrentHashMap<String, ResourceInfo> resources;

    public FogResourceServiceImpl() {
        resources = new ConcurrentHashMap<>();
    }

    @Override
    public void setResourceRestriction(FogIdentification fogIdentification, ResourceInfo resourceInfo) {
        if (resourceInfo == null) {
            resourceInfo = getDefaultResourceInfo();
        }
        resources.put(fogIdentification.toFogId(), resourceInfo);
    }

    private ResourceInfo getDefaultResourceInfo() {
        return new ResourceInfo(1000, 1000, 1000, 1000);
    }

    @Override
    public ResourceInfo getAvailableResources(FogIdentification fogIdentification) {
        ResourceInfo resourceInfo = resources.getOrDefault(fogIdentification.toFogId(), getDefaultResourceInfo());
        return resourceInfo;
    }

    @Override
    public void reset() {
        resources.clear();
    }
}
