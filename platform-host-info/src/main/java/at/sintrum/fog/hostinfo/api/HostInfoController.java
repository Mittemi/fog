package at.sintrum.fog.hostinfo.api;

import at.sintrum.fog.core.service.EnvironmentInfoService;
import at.sintrum.fog.hostinfo.dto.HostInfoDto;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Michael Mittermayr on 24.05.2017.
 */
@RestController
public class HostInfoController implements HostInfoApi {

    private EnvironmentInfoService environmentInfoService;

    public HostInfoController(EnvironmentInfoService environmentInfoService) {
        this.environmentInfoService = environmentInfoService;
    }

    @Override
    public HostInfoDto getHostinfo() {
        return new HostInfoDto(environmentInfoService.getOwnContainerId(), environmentInfoService.isInsideContainer());
    }
}
