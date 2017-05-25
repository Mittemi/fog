package at.sintrum.fog.hostinfo.api;

import at.sintrum.fog.hostinfo.dto.HostInfoDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Michael Mittermayr on 24.05.2017.
 */
@RequestMapping(value = "platform/")
public interface HostInfoApi {

    @RequestMapping(value = "hostinfo", method = RequestMethod.GET)
    HostInfoDto getHostinfo();
}
