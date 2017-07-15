package at.sintrum.fog.application.core.api;

import at.sintrum.fog.application.core.api.dto.AppInfo;
import at.sintrum.fog.applicationhousing.api.dto.AppIdentification;
import at.sintrum.fog.applicationhousing.api.dto.AppUpdateInfo;
import at.sintrum.fog.applicationhousing.client.api.AppEvolution;
import at.sintrum.fog.core.service.EnvironmentInfoService;
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

    public ApplicationInfo(AppEvolution appEvolution, EnvironmentInfoService environmentInfoService) {
        this.appEvolution = appEvolution;
        this.environmentInfoService = environmentInfoService;
    }


    @RequestMapping(value = "info", method = RequestMethod.GET)
    public AppInfo info() {
        AppUpdateInfo appUpdateInfo = appEvolution.checkForUpdate(new AppIdentification(environmentInfoService.getMetadataId()));

        AppInfo appInfo = new AppInfo();
        appInfo.setRequiresUpdate(appUpdateInfo.isUpdateRequired());
        appInfo.setMetadataId(environmentInfoService.getMetadataId());
        return appInfo;
    }
}
