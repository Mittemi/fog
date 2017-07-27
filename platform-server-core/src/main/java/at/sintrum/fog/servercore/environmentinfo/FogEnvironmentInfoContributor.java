package at.sintrum.fog.servercore.environmentinfo;

import at.sintrum.fog.core.service.EnvironmentInfoService;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

/**
 * Created by Michael Mittermayr on 26.07.2017.
 */
@Component
public class FogEnvironmentInfoContributor implements InfoContributor {

    private final EnvironmentInfoService environmentInfoService;

    public FogEnvironmentInfoContributor(EnvironmentInfoService environmentInfoService) {
        this.environmentInfoService = environmentInfoService;
    }

    @Override
    public void contribute(Info.Builder builder) {

        EnvironmentInfo info = new EnvironmentInfo();
        info.setActiveProfile(environmentInfoService.getServiceProfile());
        info.setFogId(environmentInfoService.getFogId());
        info.setContainerId(environmentInfoService.getOwnContainerId());
        info.setApplicationName(environmentInfoService.getApplicationName());
        info.setEurekaClientIp(environmentInfoService.getEurekaClientIp());
        info.setEurekaServiceUrl(environmentInfoService.getEurekaServiceUrl());
        info.setIsInsideContainer(environmentInfoService.isInsideContainer());
        info.setIsCloud(environmentInfoService.isCloud());
        info.setSwaggerUrl(environmentInfoService.getOwnUrl() + "/swagger-ui.html");

        builder.withDetail("fog", info);
    }
}
