package at.sintrum.fog.applicationhousing.api;

import at.sintrum.fog.applicationhousing.api.dto.RecoveryStateInfo;
import at.sintrum.fog.applicationhousing.recovery.ApplicationRecovery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Michael Mittermayr on 04.09.2017.
 */
@RestController
public class AppRecoveryController implements AppRecoveryApi {

    private final ApplicationRecovery applicationRecovery;

    @Autowired(required = false)
    public AppRecoveryController(ApplicationRecovery applicationRecovery) {
        this.applicationRecovery = applicationRecovery;
    }

    @Override
    public void reset() {
        if (applicationRecovery != null) {
            applicationRecovery.reset();
        }
    }

    @Override
    public RecoveryStateInfo getState() {
        return null;        //TODO: impl
    }
}
