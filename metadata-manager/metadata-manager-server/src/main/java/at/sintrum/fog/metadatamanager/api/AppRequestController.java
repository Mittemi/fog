package at.sintrum.fog.metadatamanager.api;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.metadatamanager.api.dto.AppRequest;
import at.sintrum.fog.metadatamanager.api.dto.AppRequestDto;
import at.sintrum.fog.metadatamanager.api.dto.AppRequestResult;
import at.sintrum.fog.metadatamanager.api.dto.RequestState;
import at.sintrum.fog.metadatamanager.service.requests.AppRequestServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Michael Mittermayr on 28.10.2017.
 */
@RestController
public class AppRequestController implements AppRequestsApi {

    private final Logger LOG = LoggerFactory.getLogger(AppRequestController.class);

    private final AppRequestServiceImpl appRequestService;

    public AppRequestController(AppRequestServiceImpl appRequestService) {
        this.appRequestService = appRequestService;
    }

    @Override
    public AppRequestResult request(@RequestBody AppRequestDto appRequestDto) {
        AppRequest appRequest = appRequestDto.getAppRequest();

        LOG.debug("Request move for app: " + appRequest.getInstanceId() + " to target " + appRequest.getTarget().toFogId());
        AppRequestResult result = appRequestService.request(appRequestDto.getCredits(), appRequestDto.getInternalId(), appRequest);
        LOG.debug("New credits for: " + appRequest.getInstanceId() + ", " + result.getCreditsTotal() + "(" + result.getInternalId() + ")");
        return result;
    }

    @Override
    public List<AppRequest> getRequests(@PathVariable("appName") String appName) {
        return null;
    }

    @Override
    public List<String> getKnownApps() {
        return appRequestService.getKnownApps();
    }

    @Override
    public AppRequest getNextRequest(@PathVariable("instanceId") String instanceId) {
        LOG.debug("Get next request for: " + instanceId);
        AppRequest nextRequest = appRequestService.getNextRequest(instanceId);
        if (nextRequest != null) {
            LOG.debug("Next request: " + nextRequest.getTarget().toFogId());
        } else {
            LOG.debug("No new target");
        }
        return nextRequest;
    }

    @Override
    public AppRequest finishMove(@PathVariable("instanceId") String instanceId, @RequestBody FogIdentification currentFog) {
        LOG.debug("Finish move for app: " + instanceId + " to target " + currentFog.toFogId());
        return appRequestService.finishMove(instanceId, currentFog);
    }

    @Override
    public RequestState getRequestState(@PathVariable("internalId") String internalId) {
        return appRequestService.requestInfo(internalId);
    }

    @Override
    public void reset() {
        appRequestService.reset();
    }
}
