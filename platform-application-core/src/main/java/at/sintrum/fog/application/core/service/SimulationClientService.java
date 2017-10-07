package at.sintrum.fog.application.core.service;

import at.sintrum.fog.core.dto.FogIdentification;

/**
 * Created by Michael Mittermayr on 09.08.2017.
 */
public interface SimulationClientService {

    void sendHeartbeat();

    void notifyStarting();

    void notifyMoving(FogIdentification target);

    void notifyMoved();

    void notifyStandby();

    void notifyUpgrade();
}
