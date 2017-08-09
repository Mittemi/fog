package at.sintrum.fog.simulation.appclient;

import at.sintrum.fog.core.dto.FogIdentification;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Michael Mittermayr on 08.08.2017.
 */
@RequestMapping(value = "request")
public interface PlatformAppClient {

    @RequestMapping(value = "move", method = RequestMethod.POST)
    boolean requestApplication(@RequestBody FogIdentification fogIdentification);
}