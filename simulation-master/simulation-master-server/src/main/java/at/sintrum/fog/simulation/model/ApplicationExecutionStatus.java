package at.sintrum.fog.simulation.model;

import at.sintrum.fog.core.dto.FogIdentification;

/**
 * Created by Michael Mittermayr on 08.08.2017.
 */
public class ApplicationExecutionStatus {

    private String metadataId;

    private FogIdentification currentEnvironment;

    private SimulationState simulationState;


    public ApplicationExecutionStatus() {
        simulationState = SimulationState.Starting;
    }

    public String getMetadataId() {
        return metadataId;
    }

    public void setMetadataId(String metadataId) {
        this.metadataId = metadataId;
    }

    public FogIdentification getCurrentEnvironment() {
        return currentEnvironment;
    }

    public void setCurrentEnvironment(FogIdentification currentEnvironment) {
        this.currentEnvironment = currentEnvironment;
    }

    public SimulationState getSimulationState() {
        return simulationState;
    }

    public void setSimulationState(SimulationState simulationState) {
        this.simulationState = simulationState;
    }
}
