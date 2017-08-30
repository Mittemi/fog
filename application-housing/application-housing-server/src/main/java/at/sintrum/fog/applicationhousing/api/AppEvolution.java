package at.sintrum.fog.applicationhousing.api;

import at.sintrum.fog.applicationhousing.api.dto.AppIdentification;
import at.sintrum.fog.applicationhousing.api.dto.AppUpdateInfo;
import at.sintrum.fog.applicationhousing.api.dto.AppUpdateMetadata;
import at.sintrum.fog.applicationhousing.service.UpdateMetadataService;
import at.sintrum.fog.servercore.connectionfilter.SimulationControlledOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Michael Mittermayr on 14.07.2017.
 */
@RestController
public class AppEvolution implements AppEvolutionApi {

    private Logger LOG = LoggerFactory.getLogger(AppEvolution.class);

    private final UpdateMetadataService updateMetadataService;

    public AppEvolution(UpdateMetadataService updateMetadataService) {
        this.updateMetadataService = updateMetadataService;
    }

    @Override
    @SimulationControlledOperation
    public AppUpdateInfo checkForUpdate(@RequestBody AppIdentification appIdentification) {
        LOG.debug("Check update for: " + appIdentification.getImageMetadataId());

        return updateMetadataService.getUpdateInfo(appIdentification);
    }

    @Override
    public void setUpdateMetadata(AppUpdateMetadata appUpdateMetadata) {
        updateMetadataService.addUpdateMetadata(appUpdateMetadata.getCurrent(), appUpdateMetadata.getUpdated());
    }

    @Override
    public void removeUpdate(AppIdentification appIdentification) {
        updateMetadataService.removeUpdate(appIdentification);
    }

    @RequestMapping(value = "reset", method = RequestMethod.POST)
    public void reset() {
        updateMetadataService.reset();
    }
}
