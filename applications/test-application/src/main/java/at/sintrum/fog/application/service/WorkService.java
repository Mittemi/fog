package at.sintrum.fog.application.service;

import at.sintrum.fog.application.core.service.ApplicationLifecycleService;
import at.sintrum.fog.application.model.WorkStatus;
import at.sintrum.fog.core.service.EnvironmentInfoService;
import org.bouncycastle.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Michael Mittermayr on 01.08.2017.
 */
@Service
public class WorkService {

    private final Random random = new Random();
    private final String[] SAYINGS = {
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

    private boolean isDone = false;
    private boolean allowToMove = false;

    private final ApplicationLifecycleService applicationLifecycleService;
    private final EnvironmentInfoService environmentInfoService;
    private static final Logger LOG = LoggerFactory.getLogger(WorkService.class);

    public WorkService(ApplicationLifecycleService applicationLifecycleService, EnvironmentInfoService environmentInfoService) {
        this.applicationLifecycleService = applicationLifecycleService;
        this.environmentInfoService = environmentInfoService;
    }

    @Scheduled(fixedDelay = 1000)
    public void moveAppToNextTargetAfterTimeout() throws InterruptedException {
        if (isDone && allowToMove) {
            Thread.sleep(1000);     //just a workaround to finish the currently running request
            applicationLifecycleService.workIsFinished();
        }
    }

    public String doWork() {
        synchronized (this) {
            if (isDone) {
                allowToMove = true;     // not really required
                return "You shall not pass! Just kidding, I am already done here. Just waiting to move :)";
            }
            isDone = true;
        }

        String result = SAYINGS[random.nextInt(SAYINGS.length)];

        if (environmentInfoService.isInsideContainer()) {
            try {
                File file;
                do {
                    String path = "/app/storage/" + UUID.randomUUID().toString();
                    file = new File(path);
                }
                while (file.exists());

                try (FileOutputStream outputStream = new FileOutputStream(file)) {
                    outputStream.write(Strings.toByteArray(result));
                }

            } catch (Exception ex) {
                LOG.error("Failed during work", ex);
            }
        }
        allowToMove = true;
        return result;
    }

    public WorkStatus getWorkStatus() {
        if (!environmentInfoService.isInsideContainer()) {
            return new WorkStatus();
        }

        File f = new File("/app/storage");

        File[] files = f.listFiles();
        return new WorkStatus(files == null ? 0 : files.length);
    }
}
