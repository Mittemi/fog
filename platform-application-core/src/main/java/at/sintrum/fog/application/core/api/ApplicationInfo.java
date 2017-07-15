package at.sintrum.fog.application.core.api;

import at.sintrum.fog.application.core.api.dto.AppInfo;
import at.sintrum.fog.applicationhousing.api.dto.AppIdentification;
import at.sintrum.fog.applicationhousing.api.dto.AppUpdateInfo;
import at.sintrum.fog.applicationhousing.client.api.AppEvolution;
import at.sintrum.fog.core.service.EnvironmentInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Michael Mittermayr on 15.07.2017.
 */
@RestController
@RequestMapping("app/")
public class ApplicationInfo {

    private final AppEvolution appEvolution;
    private final EnvironmentInfoService environmentInfoService;

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationInfo.class);

    public ApplicationInfo(AppEvolution appEvolution, EnvironmentInfoService environmentInfoService) {
        this.appEvolution = appEvolution;
        this.environmentInfoService = environmentInfoService;
    }


    @RequestMapping(value = "info", method = RequestMethod.GET)
    public AppInfo info() {

        AppInfo appInfo = new AppInfo();

        try {
            AppUpdateInfo appUpdateInfo = appEvolution.checkForUpdate(new AppIdentification(environmentInfoService.getMetadataId()));
            appInfo.setRequiresUpdate(appUpdateInfo.isUpdateRequired());
        } catch (Exception ex) {
            LOG.error("Failed to call AppEvolution: ", ex);
            appInfo.setMessage(ex.getMessage());
        }

        appInfo.setMetadataId(environmentInfoService.getMetadataId());
        appInfo.setActiveProfiles(environmentInfoService.getServiceProfile());
        return appInfo;
    }
}
