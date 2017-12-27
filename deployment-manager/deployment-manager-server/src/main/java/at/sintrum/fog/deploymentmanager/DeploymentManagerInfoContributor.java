package at.sintrum.fog.deploymentmanager;

import at.sintrum.fog.deploymentmanager.service.ApplicationManagerService;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

/**
 * Created by Michael Mittermayr on 27.12.2017.
 */
@Component
public class DeploymentManagerInfoContributor implements InfoContributor {

    private ApplicationManagerService applicationManagerService;

    public DeploymentManagerInfoContributor(ApplicationManagerService applicationManagerService) {
        this.applicationManagerService = applicationManagerService;
    }

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("DM", applicationManagerService.getUsedResources());
    }
}
