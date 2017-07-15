package at.sintrum.fog.applicationhousing.api;

import at.sintrum.fog.applicationhousing.api.dto.AppIdentification;
import at.sintrum.fog.applicationhousing.api.dto.AppUpdateInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Michael Mittermayr on 14.07.2017.
 */
@RestController
public class AppEvolution implements AppEvolutionApi {

    private Logger LOG = LoggerFactory.getLogger(AppEvolution.class);

    @Override
    public AppUpdateInfo isUpdateRequired(AppIdentification appIdentification) {
        LOG.debug("Check update for: " + appIdentification.getImageMetadataId());
        return new AppUpdateInfo();
    }
}
