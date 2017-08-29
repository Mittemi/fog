package at.sintrum.fog.deploymentmanager.service;

import at.sintrum.fog.core.dto.ResourceInfo;
import at.sintrum.fog.deploymentmanager.api.dto.*;

import java.util.concurrent.Future;

/**
 * Created by Michael on 2017-06-29.
 */
public interface ApplicationManagerService {
    Future<FogOperationResult> start(ApplicationStartRequest applicationStartRequest);

    Future<FogOperationResult> move(ApplicationMoveRequest applicationMoveRequest);

    Future<FogOperationResult> upgrade(ApplicationUpgradeRequest applicationUpgradeRequest);

    Future<FogOperationResult> recover(ApplicationRecoveryRequest applicationRecoveryRequest);

    Future<FogOperationResult> remove(ApplicationRemoveRequest applicationRemoveRequest);

    boolean checkResources(ResourceInfo resourceInfo);
}
