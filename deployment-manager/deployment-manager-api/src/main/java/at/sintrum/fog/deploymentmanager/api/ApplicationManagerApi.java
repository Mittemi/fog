package at.sintrum.fog.deploymentmanager.api;

import at.sintrum.fog.core.dto.ResourceInfo;
import at.sintrum.fog.deploymentmanager.api.dto.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Michael Mittermayr on 31.05.2017.
 */
@RequestMapping(value = "application/")
public interface ApplicationManagerApi {

    @RequestMapping(value = "start", method = RequestMethod.POST)
    FogOperationResult requestApplicationStart(@RequestBody ApplicationStartRequest startRequest);

    @RequestMapping(value = "move", method = RequestMethod.POST)
    FogOperationResult moveApplication(@RequestBody ApplicationMoveRequest applicationMoveRequest);

    @RequestMapping(value = "upgrade", method = RequestMethod.POST)
    FogOperationResult upgradeApplication(@RequestBody ApplicationUpgradeRequest applicationUpgradeRequest);

    @RequestMapping(value = "recover", method = RequestMethod.POST)
    FogOperationResult recoverApplication(@RequestBody ApplicationRecoveryRequest applicationRecoveryRequest);

    @RequestMapping(value = "remove", method = RequestMethod.DELETE)
    FogOperationResult removeApplication(@RequestBody ApplicationRemoveRequest applicationRemoveRequest);

    @RequestMapping(value = "checkResources", method = RequestMethod.POST)
    boolean checkResources(@RequestBody ResourceInfo resourceInfo);

    @RequestMapping(value = "isAlive", method = RequestMethod.GET)
    boolean isAlive();

    @RequestMapping(value = "reset", method = RequestMethod.POST)
    void reset();
}
