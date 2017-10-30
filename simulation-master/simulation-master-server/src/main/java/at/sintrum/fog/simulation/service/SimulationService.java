package at.sintrum.fog.simulation.service;

import at.sintrum.fog.simulation.api.dto.AppEventInfo;
import at.sintrum.fog.simulation.simulation.AppEvent;

/**
 * Created by Michael Mittermayr on 08.08.2017.
 */
public interface SimulationService {

    void processOperation(String instanceId, AppEvent appEvent, AppEventInfo eventInfo);
}
