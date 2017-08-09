package at.sintrum.fog.application.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Michael Mittermayr on 08.08.2017.
 */
@RequestMapping(value = "work")
public interface WorkApi {

    @RequestMapping(value = "doSomeWork", method = RequestMethod.POST)
    String doSomeWork();
}
