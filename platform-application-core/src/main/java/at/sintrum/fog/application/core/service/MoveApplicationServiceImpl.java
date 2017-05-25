package at.sintrum.fog.application.core.service;

import at.sintrum.fog.core.service.EnvironmentInfoService;
import org.springframework.stereotype.Service;

/**
 * Created by Michael Mittermayr on 24.05.2017.
 */
@Service
public class MoveApplicationServiceImpl implements MoveApplicationService {

    private final EnvironmentInfoService environmentInfoService;

    public MoveApplicationServiceImpl(EnvironmentInfoService environmentInfoService) {
        this.environmentInfoService = environmentInfoService;
    }

    @Override
    public void moveApplication(String targetIp, String targetPort) {
        String containerId = environmentInfoService.getOwnContainerId();


    }
}
