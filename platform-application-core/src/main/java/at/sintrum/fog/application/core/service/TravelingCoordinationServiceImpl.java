package at.sintrum.fog.application.core.service;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.core.service.EnvironmentInfoService;
import at.sintrum.fog.metadatamanager.api.dto.AppRequest;
import at.sintrum.fog.metadatamanager.client.api.AppRequestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Michael Mittermayr on 17.07.2017.
 */
@Service
public class TravelingCoordinationServiceImpl implements TravelingCoordinationService {

    private static final Logger LOG = LoggerFactory.getLogger(TravelingCoordinationServiceImpl.class);

    private final EnvironmentInfoService environmentInfoService;

    private final AppRequestClient appRequestClient;

    public TravelingCoordinationServiceImpl(EnvironmentInfoService environmentInfoService, AppRequestClient appRequestClient) {
        this.environmentInfoService = environmentInfoService;
        this.appRequestClient = appRequestClient;
    }

    @Override
    public List<FogIdentification> getTargets() {
        return appRequestClient.getRequests(environmentInfoService.getApplicationName()).stream().map(AppRequest::getTarget).collect(Collectors.toList());
    }

    @Override
    public FogIdentification getNextTarget() {

        try {
            AppRequest request = appRequestClient.getNextRequest(environmentInfoService.getInstanceId());

            return request != null ? request.getTarget() : null;
        } catch (Exception ex) {
            LOG.error("Failed to get next travel target", ex);
            return null;
        }
    }

    @Override
    public boolean startMove(FogIdentification target) {

        return true;
    }

    @Override
    public boolean finishMove(FogIdentification currentFog) {

        AppRequest appRequest = appRequestClient.finishMove(environmentInfoService.getInstanceId(), currentFog);

        return appRequest != null;
    }
}
