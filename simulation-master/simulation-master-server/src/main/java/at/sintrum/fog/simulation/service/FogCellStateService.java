package at.sintrum.fog.simulation.service;

import at.sintrum.fog.core.dto.FogIdentification;

/**
 * Created by Michael Mittermayr on 30.08.2017.
 */
public interface FogCellStateService {

    boolean isOnline(FogIdentification fogIdentification);

    void setFogNetworkState(FogIdentification fogIdentification, boolean isOnline);

    void setFogServiceState(FogIdentification fogIdentification, boolean isOnline);

    void reset();
}
