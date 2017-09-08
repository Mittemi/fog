package at.sintrum.fog.simulation.scenario.dto;

import at.sintrum.fog.core.dto.FogIdentification;

/**
 * Created by Michael Mittermayr on 31.08.2017.
 */
public class BasicScenarioInfo {

    private FogIdentification cloud;

    private FogIdentification fogA;

    private FogIdentification fogB;

    private FogIdentification fogC;

    public FogIdentification getCloud() {
        return cloud;
    }

    public void setCloud(FogIdentification cloud) {
        this.cloud = cloud;
    }

    public FogIdentification getFogA() {
        return fogA;
    }

    public void setFogA(FogIdentification fogA) {
        this.fogA = fogA;
    }

    public FogIdentification getFogB() {
        return fogB;
    }

    public void setFogB(FogIdentification fogB) {
        this.fogB = fogB;
    }

    public FogIdentification getFogC() {
        return fogC;
    }

    public void setFogC(FogIdentification fogC) {
        this.fogC = fogC;
    }
}
