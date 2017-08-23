package at.sintrum.fog.application.core.api;

import at.sintrum.fog.application.core.api.dto.AppInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Michael Mittermayr on 23.08.2017.
 */
@RequestMapping("app/")
public interface ApplicationInfoApi {

    @RequestMapping(value = "info", method = RequestMethod.GET)
    AppInfo info();
}
