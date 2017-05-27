package at.sintrum.fog.application.api;

import at.sintrum.fog.application.core.service.MoveApplicationService;
import at.sintrum.fog.application.model.MoveApplicationRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Michael Mittermayr on 24.05.2017.
 */
@RestController
@RequestMapping(value = "/app")
public class ApplicationController {

    private final MoveApplicationService moveApplicationService;

    public ApplicationController(MoveApplicationService moveApplicationService) {
        this.moveApplicationService = moveApplicationService;
    }


    @RequestMapping(value = "move", method = RequestMethod.POST)
    public void moveApplication(MoveApplicationRequest moveApplicationRequest) {
        moveApplicationService.moveApplication(moveApplicationRequest.getTargetIp(), moveApplicationRequest.getTargetPort());
    }

}
