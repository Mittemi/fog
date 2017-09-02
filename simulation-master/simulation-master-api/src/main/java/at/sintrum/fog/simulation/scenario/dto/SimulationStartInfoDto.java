package at.sintrum.fog.simulation.scenario.dto;

import at.sintrum.fog.core.dto.FogIdentification;

/**
 * Created by Michael Mittermayr on 08.08.2017.
 */
public class SimulationStartInfoDto {

    private FogIdentification cloud;

    private FogIdentification[] fogs;

    private String metadataId;

    public FogIdentification getCloud() {
        return cloud;
    }

    public void setCloud(FogIdentification cloud) {
        this.cloud = cloud;
    }

    public String getMetadataId() {
        return metadataId;
    }

    public void setMetadataId(String metadataId) {
        this.metadataId = metadataId;
    }

    public FogIdentification[] getFogs() {
        return fogs;
    }

    public void setFogs(FogIdentification[] fogs) {
        this.fogs = fogs;
    }
}
