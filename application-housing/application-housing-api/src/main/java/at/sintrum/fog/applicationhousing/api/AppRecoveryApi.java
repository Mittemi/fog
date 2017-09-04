package at.sintrum.fog.applicationhousing.api;

import at.sintrum.fog.applicationhousing.api.dto.RecoveryStateInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Michael Mittermayr on 04.09.2017.
 */
@RequestMapping(value = "recovery")
public interface AppRecoveryApi {

    @RequestMapping(value = "reset", method = RequestMethod.POST)
    void reset();

    @RequestMapping(value = "", method = RequestMethod.GET)
    RecoveryStateInfo getState();
}
