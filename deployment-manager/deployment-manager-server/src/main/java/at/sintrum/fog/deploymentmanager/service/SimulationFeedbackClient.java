package at.sintrum.fog.deploymentmanager.service;

import at.sintrum.fog.deploymentmanager.api.dto.*;

/**
 * Created by Michael Mittermayr on 09.09.2017.
 */
public interface SimulationFeedbackClient {
    void appStart(ApplicationStartRequest applicationStartRequest, FogOperationResult fogOperationResult, String containerId);

    void appMove(ApplicationMoveRequest applicationMoveRequest, FogOperationResult fogOperationResult, String containerId);

    void appUpgrade(ApplicationUpgradeRequest applicationUpgradeRequest, FogOperationResult fogOperationResult, String containerId);

    void appRecover(ApplicationRecoveryRequest applicationRecoveryRequest, FogOperationResult fogOperationResult, String containerId);

    void appRemove(ApplicationRemoveRequest applicationRemoveRequest, FogOperationResult fogOperationResult, String containerId);
}
