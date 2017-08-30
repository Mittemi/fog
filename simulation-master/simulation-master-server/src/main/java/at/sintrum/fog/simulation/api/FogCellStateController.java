package at.sintrum.fog.simulation.api;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.simulation.service.FogCellStateService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Michael Mittermayr on 30.08.2017.
 */
@RestController
public class FogCellStateController implements FogCellStateApi {

    private final FogCellStateService fogCellStateService;

    public FogCellStateController(FogCellStateService fogCellStateService) {
        this.fogCellStateService = fogCellStateService;
    }

    @Override
    public boolean isOnline(@RequestBody FogIdentification fogIdentification) {
        return fogCellStateService.isOnline(fogIdentification);
    }
}
