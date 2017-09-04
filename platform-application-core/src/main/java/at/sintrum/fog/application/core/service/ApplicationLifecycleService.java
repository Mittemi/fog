package at.sintrum.fog.application.core.service;

/**
 * Created by Michael Mittermayr on 24.05.2017.
 */
public interface ApplicationLifecycleService {

    boolean moveAppIfRequired();

    boolean upgradeAppIfRequired();

    boolean shouldAcceptRequests();

    boolean tearDown();
}
