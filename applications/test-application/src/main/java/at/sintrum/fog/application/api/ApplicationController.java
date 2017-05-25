package at.sintrum.fog.application.api;

import at.sintrum.fog.application.model.MoveApplicationRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Created by Michael Mittermayr on 24.05.2017.
 */
@RestController
@RequestMapping(value = "/app")
public class ApplicationController {

    @RequestMapping(value = "move", method = RequestMethod.POST)
    public void moveApplication(MoveApplicationRequest moveApplicationRequest) {

    }

}
