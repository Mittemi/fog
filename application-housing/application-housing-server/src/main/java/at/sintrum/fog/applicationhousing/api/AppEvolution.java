package at.sintrum.fog.applicationhousing.api;

import at.sintrum.fog.applicationhousing.api.dto.AppIdentification;
import at.sintrum.fog.applicationhousing.api.dto.AppUpdateInfo;
import at.sintrum.fog.metadatamanager.api.ImageMetadataApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Michael Mittermayr on 14.07.2017.
 */
@RestController
public class AppEvolution implements AppEvolutionApi {

    private Logger LOG = LoggerFactory.getLogger(AppEvolution.class);

    private final ImageMetadataApi imageMetadataClient;

    public AppEvolution(ImageMetadataApi imageMetadataClient) {
        this.imageMetadataClient = imageMetadataClient;
    }

    @Override
    public AppUpdateInfo checkForUpdate(@RequestBody AppIdentification appIdentification) {
        LOG.debug("Check update for: " + appIdentification.getImageMetadataId());

        imageMetadataClient.getById(appIdentification.getImageMetadataId());

        AppUpdateInfo appUpdateInfo = new AppUpdateInfo();
        appUpdateInfo.setUpdateRequired(true);
        appUpdateInfo.setImageMetadataId(appIdentification.getImageMetadataId());

        return appUpdateInfo;
    }
}
