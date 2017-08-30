package at.sintrum.fog.simulation.api;

import at.sintrum.fog.core.dto.FogIdentification;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Michael Mittermayr on 30.08.2017.
 */
@RequestMapping("fogcellstate")
public interface FogCellStateApi {

    @RequestMapping(value = "isOnline", method = RequestMethod.POST)
    boolean isOnline(@RequestBody FogIdentification fogIdentification);
}
