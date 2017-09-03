package at.sintrum.fog.application.core.service;

import at.sintrum.fog.core.dto.FogIdentification;

import java.util.List;

/**
 * Created by Michael Mittermayr on 17.07.2017.
 */
public interface TravelingCoordinationService {

    boolean requestMove(FogIdentification fogIdentification);

    List<FogIdentification> getTargets();

    boolean hasNextTarget();

    FogIdentification getNextTarget();

    boolean startMove(FogIdentification target);

    boolean finishMove(FogIdentification currentFog);

    void reset();
}
