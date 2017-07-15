package at.sintrum.fog.applicationhousing.api;

import at.sintrum.fog.applicationhousing.api.dto.AppIdentification;
import at.sintrum.fog.applicationhousing.api.dto.AppUpdateInfo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Michael Mittermayr on 14.07.2017.
 */
@RequestMapping(value = "evolution/")
public interface AppEvolutionApi {

    @RequestMapping(value = "checkForUpdate", method = RequestMethod.POST)
    AppUpdateInfo checkForUpdate(@RequestBody AppIdentification appIdentification);
}
