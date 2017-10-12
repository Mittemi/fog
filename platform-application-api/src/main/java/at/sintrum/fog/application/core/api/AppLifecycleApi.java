package at.sintrum.fog.application.core.api;

import at.sintrum.fog.application.core.api.dto.RequestAppDto;
import at.sintrum.fog.core.dto.FogIdentification;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by Michael Mittermayr on 23.08.2017.
 */
@RequestMapping(value = "lifecycle")
public interface AppLifecycleApi {

    @RequestMapping(value = "move", method = RequestMethod.POST)
    boolean requestApplication(@RequestBody RequestAppDto requestAppDto);

    @RequestMapping(value = "teardown", method = RequestMethod.DELETE)
    boolean tearDownApplication();

    @RequestMapping(value = "getTravelQueue", method = RequestMethod.GET)
    List<FogIdentification> getTravelQueue();
}
