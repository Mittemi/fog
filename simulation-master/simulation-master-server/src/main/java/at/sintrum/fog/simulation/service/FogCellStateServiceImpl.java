package at.sintrum.fog.simulation.service;

import at.sintrum.fog.core.dto.FogIdentification;
import org.springframework.stereotype.Service;

/**
 * Created by Michael Mittermayr on 30.08.2017.
 */
@Service
public class FogCellStateServiceImpl implements FogCellStateService {

    @Override
    public boolean isOnline(FogIdentification fogIdentification) {
        return true;
    }
}
