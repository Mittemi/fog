package at.sintrum.fog.application.api;

import at.sintrum.fog.application.core.service.ApplicationLifecycleService;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * Created by Michael Mittermayr on 15.07.2017.
 */
@RestController
@RequestMapping(value = "work")
@Profile("!standby")
public class DoSomeWorkController {

    private static final Random random = new Random();
    private static final String[] SAYINGS = {
            "My job is secure..nobody wants it",
            "Always give 100% at work: 12% Monday, 23% Tuesday, 40% Wednesday, 20% Thursday, 5% Friday",
            "Do not disturb: I’m feeding my virtual sheep",
            "OK a monkey could do my job, but I was here first",
            "You don’t have to be crazy to work here…but it helps!",
            "You pretend to work, and we’ll pretend to pay you.",
            "Work: That annoying time between naps and coffee breaks",
            "Chaos, Panic, Disorder! My work here is done",
            "Never do today that which will become someone elses responsibility tomorrow.",
            "I’m not bossy I just know what you should be doing",
            "If work was so good, the rich would have kept more of it for themselves."
    };

    private final ApplicationLifecycleService applicationLifecycleService;

    public DoSomeWorkController(ApplicationLifecycleService applicationLifecycleService) {
        this.applicationLifecycleService = applicationLifecycleService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String doSomeWork() {
        // TODO: improve this impl.
        applicationLifecycleService.moveAppIfRequired();     //should trigger in about 1 sec. therefore enough time to return the string
        return SAYINGS[random.nextInt(SAYINGS.length)];
    }
}
