package at.sintrum.fog.application.api;

import at.sintrum.fog.application.core.service.ApplicationLifecycleService;
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

    private final ApplicationLifecycleService applicationLifecycleService;

    public ApplicationController(ApplicationLifecycleService applicationLifecycleService) {
        this.applicationLifecycleService = applicationLifecycleService;
    }


    @RequestMapping(value = "move", method = RequestMethod.POST)
    public void moveApplication(MoveApplicationRequest moveApplicationRequest) {
        applicationLifecycleService.moveApplication(moveApplicationRequest.getTarget());
    }

}
