package at.sintrum.fog.simulation.api.dto;

import at.sintrum.fog.core.dto.FogIdentification;

/**
 * Created by Michael Mittermayr on 31.08.2017.
 */
public class BasicScenarioInfoDto {

    private FogIdentification cloud;

    private FogIdentification fogA;

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
}
