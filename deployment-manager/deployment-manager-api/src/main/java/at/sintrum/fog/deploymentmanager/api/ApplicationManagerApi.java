package at.sintrum.fog.deploymentmanager.api;

import at.sintrum.fog.deploymentmanager.api.dto.ApplicationMoveRequest;
import at.sintrum.fog.deploymentmanager.api.dto.ApplicationStartRequest;
import at.sintrum.fog.deploymentmanager.api.dto.FogOperationResult;
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
}
