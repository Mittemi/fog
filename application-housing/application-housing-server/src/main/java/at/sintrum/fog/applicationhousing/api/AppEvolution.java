package at.sintrum.fog.applicationhousing.api;

import at.sintrum.fog.applicationhousing.api.dto.AppIdentification;
import at.sintrum.fog.applicationhousing.api.dto.AppInstanceIdHistoryInfo;
import at.sintrum.fog.applicationhousing.api.dto.AppUpdateInfo;
import at.sintrum.fog.applicationhousing.api.dto.AppUpdateMetadata;
import at.sintrum.fog.applicationhousing.service.InstanceIdHistoryService;
import at.sintrum.fog.applicationhousing.service.UpdateMetadataService;
import at.sintrum.fog.servercore.connectionfilter.SimulationControlledOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Michael Mittermayr on 14.07.2017.
 */
@RestController
public class AppEvolution implements AppEvolutionApi {

    private Logger LOG = LoggerFactory.getLogger(AppEvolution.class);

    private final UpdateMetadataService updateMetadataService;
    private final InstanceIdHistoryService instanceIdHistoryService;

    public AppEvolution(UpdateMetadataService updateMetadataService, InstanceIdHistoryService instanceIdHistoryService) {
        this.updateMetadataService = updateMetadataService;
        this.instanceIdHistoryService = instanceIdHistoryService;
    }

    @Override
    @SimulationControlledOperation
    public AppUpdateInfo checkForUpdate(@RequestBody AppIdentification appIdentification) {
        LOG.debug("Check update for: " + appIdentification.getImageMetadataId());

        return updateMetadataService.getUpdateInfo(appIdentification);
    }

    @Override
    public void setUpdateMetadata(@RequestBody AppUpdateMetadata appUpdateMetadata) {
        updateMetadataService.addUpdateMetadata(appUpdateMetadata.getCurrent(), appUpdateMetadata.getUpdated());
    }

    @Override
    public void removeUpdate(@RequestBody AppIdentification appIdentification) {
        updateMetadataService.removeUpdate(appIdentification);
    }

    @Override
    public boolean saveInstanceIdHistory(@RequestBody AppInstanceIdHistoryInfo upgradeHistoryInfo) {
        return instanceIdHistoryService.addToInstanceIdHistory(upgradeHistoryInfo);
    }

    @Override
    public void rollbackInstanceIdHistory(@RequestBody AppInstanceIdHistoryInfo upgradeHistoryInfo) {
        instanceIdHistoryService.rollbackInstanceIdHistory(upgradeHistoryInfo);
    }

    @Override
    public String getLatestInstanceId(@PathVariable("instanceId") String instanceId) {
        return instanceIdHistoryService.getLatestInstanceId(instanceId);
    }

    @RequestMapping(value = "reset", method = RequestMethod.POST)
    public void reset() {
        updateMetadataService.reset();
        instanceIdHistoryService.reset();
    }
}
