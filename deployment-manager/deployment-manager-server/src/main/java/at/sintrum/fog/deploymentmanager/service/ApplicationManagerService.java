package at.sintrum.fog.deploymentmanager.service;

import at.sintrum.fog.deploymentmanager.api.dto.ApplicationMoveRequest;
import at.sintrum.fog.deploymentmanager.api.dto.ApplicationStartRequest;
import at.sintrum.fog.deploymentmanager.api.dto.FogOperationResult;

/**
 * Created by Michael on 2017-06-29.
 */
public interface ApplicationManagerService {
    FogOperationResult start(ApplicationStartRequest applicationStartRequest);

    FogOperationResult move(ApplicationMoveRequest applicationMoveRequest);
}
