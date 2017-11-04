package at.sintrum.fog.metadatamanager.api;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.metadatamanager.api.dto.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by Michael Mittermayr on 28.10.2017.
 */
@RequestMapping(value = "requests")
public interface AppRequestsApi {

    @RequestMapping(value = "request", method = RequestMethod.POST)
    AppRequestResult request(@RequestBody AppRequestDto appRequestDto);

    @RequestMapping(value = "{appName}", method = RequestMethod.GET)
    List<AppRequest> getRequests(@PathVariable("appName") String appName);

    @RequestMapping(value = "knownApps", method = RequestMethod.GET)
    List<String> getKnownApps();

    @RequestMapping(value = "next/{instanceId}", method = RequestMethod.GET)
    AppRequest getNextRequest(@PathVariable("instanceId") String instanceId);

    @RequestMapping(value = "finishMove/{instanceId}", method = RequestMethod.POST)
    AppRequest finishMove(@PathVariable("instanceId") String instanceId, @RequestBody FogIdentification currentFog);

    @RequestMapping(value = "info/{internalId}", method = RequestMethod.GET)
    RequestState getRequestState(@PathVariable("internalId") String internalId);

    @RequestMapping(value = "reset", method = RequestMethod.DELETE)
    void reset();

    @RequestMapping(value = "isAuctionEnabled", method = RequestMethod.GET)
    boolean isAuctionEnabled();

    @RequestMapping(value = "enableAuction", method = RequestMethod.POST)
    void enableAuction();

    @RequestMapping(value = "disableAuction", method = RequestMethod.POST)
    void disableAuction();

    @RequestMapping(value = "resetApp/{appName}", method = RequestMethod.DELETE)
    void resetApp(@PathVariable("appName") String appName);

    @RequestMapping(value = "getFinishedRequests", method = RequestMethod.GET)
    List<AppRequestInfo> getFinishedRequests();

    @RequestMapping(value = "getNextRequests", method = RequestMethod.GET)
    List<AppRequestInfo> getNextRequests();
}
