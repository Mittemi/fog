package at.sintrum.fog.deploymentmanager.service;

import at.sintrum.fog.deploymentmanager.api.dto.ApplicationMoveRequest;
import at.sintrum.fog.deploymentmanager.api.dto.ApplicationStartRequest;
import at.sintrum.fog.deploymentmanager.api.dto.ApplicationUpgradeRequest;
import at.sintrum.fog.deploymentmanager.api.dto.FogOperationResult;

import java.util.concurrent.Future;

/**
 * Created by Michael on 2017-06-29.
 */
public interface ApplicationManagerService {
    Future<FogOperationResult> start(ApplicationStartRequest applicationStartRequest);

    Future<FogOperationResult> move(ApplicationMoveRequest applicationMoveRequest);

    Future<FogOperationResult> upgrade(ApplicationUpgradeRequest applicationUpgradeRequest);
}
