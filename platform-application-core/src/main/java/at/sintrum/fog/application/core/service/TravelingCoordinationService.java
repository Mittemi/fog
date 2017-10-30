package at.sintrum.fog.application.core.service;

import at.sintrum.fog.core.dto.FogIdentification;

import java.util.List;

/**
 * Created by Michael Mittermayr on 17.07.2017.
 */
public interface TravelingCoordinationService {

    List<FogIdentification> getTargets();

    FogIdentification getNextTarget();

    boolean startMove(FogIdentification target);

    boolean finishMove(FogIdentification currentFog);

    int getEstimatedWorkingTime();
}
