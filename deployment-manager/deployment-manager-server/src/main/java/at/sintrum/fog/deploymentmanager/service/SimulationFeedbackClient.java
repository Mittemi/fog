package at.sintrum.fog.deploymentmanager.service;

import at.sintrum.fog.deploymentmanager.api.dto.*;

/**
 * Created by Michael Mittermayr on 09.09.2017.
 */
public interface SimulationFeedbackClient {
    void appStart(ApplicationStartRequest applicationStartRequest, FogOperationResult fogOperationResult);

    void appMove(ApplicationMoveRequest applicationMoveRequest, FogOperationResult fogOperationResult);

    void appUpgrade(ApplicationUpgradeRequest applicationUpgradeRequest, FogOperationResult fogOperationResult);

    void appRecover(ApplicationRecoveryRequest applicationRecoveryRequest, FogOperationResult fogOperationResult);

    void appRemove(ApplicationRemoveRequest applicationRemoveRequest, FogOperationResult fogOperationResult);
}
