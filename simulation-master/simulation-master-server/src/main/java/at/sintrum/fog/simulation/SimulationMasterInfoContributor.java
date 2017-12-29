package at.sintrum.fog.simulation;

import at.sintrum.fog.simulation.scenario.evaluation.FogRequestsManager;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

/**
 * Created by Michael Mittermayr on 29.12.2017.
 */
@Component
public class SimulationMasterInfoContributor implements InfoContributor {

    private FogRequestsManager fogRequestsManager;

    @Override
    public void contribute(Info.Builder builder) {
        if (fogRequestsManager != null) {
            builder.withDetail("SM", fogRequestsManager.getState());
        }
    }

    public FogRequestsManager getFogRequestsManager() {
        return fogRequestsManager;
    }

    public void setFogRequestsManager(FogRequestsManager fogRequestsManager) {
        this.fogRequestsManager = fogRequestsManager;
    }
}
