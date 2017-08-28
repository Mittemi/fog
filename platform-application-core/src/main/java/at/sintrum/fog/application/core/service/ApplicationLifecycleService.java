package at.sintrum.fog.application.core.service;

import at.sintrum.fog.core.dto.FogIdentification;

/**
 * Created by Michael Mittermayr on 24.05.2017.
 */
public interface ApplicationLifecycleService {

    boolean moveApplication(FogIdentification target);

    void moveAppIfRequired();

    boolean upgradeAppIfRequired();

    boolean shouldAcceptRequests();

    boolean tearDown();
}
