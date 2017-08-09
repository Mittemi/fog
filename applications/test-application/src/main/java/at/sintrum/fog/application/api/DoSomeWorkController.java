package at.sintrum.fog.application.api;

import at.sintrum.fog.application.model.WorkStatus;
import at.sintrum.fog.application.service.WorkService;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Michael Mittermayr on 15.07.2017.
 */
@RestController
@Profile("!standby")
public class DoSomeWorkController implements WorkApi {

    private final WorkService workService;

    public DoSomeWorkController(WorkService workService) {
        this.workService = workService;
    }

    @RequestMapping(value = "doSomeWork", method = RequestMethod.POST)
    public String doSomeWork() {
        String result = workService.doWork();
        workService.moveAppToNextTargetAfterTimeout();      //async call
        return result;
    }

    @RequestMapping(value = "status", method = RequestMethod.GET)
    public WorkStatus workStatus() {
        return workService.getWorkStatus();
    }
}
