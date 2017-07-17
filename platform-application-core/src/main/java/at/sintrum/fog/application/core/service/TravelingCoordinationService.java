package at.sintrum.fog.application.core.service;

import at.sintrum.fog.core.dto.FogIdentification;

/**
 * Created by Michael Mittermayr on 17.07.2017.
 */
public interface TravelingCoordinationService {

    boolean requestMove(FogIdentification fogIdentification);

    boolean hasNextTarget();

    FogIdentification getNextTarget();
}
