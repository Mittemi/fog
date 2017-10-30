package at.sintrum.fog.simulation.simulation.mongo;

import at.sintrum.fog.simulation.api.dto.AppEventInfo;
import org.springframework.data.annotation.Id;

/**
 * Created by Michael Mittermayr on 30.10.2017.
 */
public class SimulationDbEntry {

    @Id
    private long id;

    private String simulationRunId;

    private AppEventInfo appEventInfo;

    public String getSimulationRunId() {
        return simulationRunId;
    }

    public void setSimulationRunId(String simulationRunId) {
        this.simulationRunId = simulationRunId;
    }

    public AppEventInfo getAppEventInfo() {
        return appEventInfo;
    }

    public void setAppEventInfo(AppEventInfo appEventInfo) {
        this.appEventInfo = appEventInfo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
