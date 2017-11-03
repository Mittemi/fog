package at.sintrum.fog.simulation.simulation.mongo;

import at.sintrum.fog.simulation.api.dto.AppEventInfo;
import at.sintrum.fog.simulation.simulation.AppEvent;
import org.springframework.data.annotation.Id;

/**
 * Created by Michael Mittermayr on 30.10.2017.
 */
public class SimulationDbEntry {

    @Id
    private String id;

    private String simulationRunId;

    private AppEventInfo appEventInfo;
    private AppEvent appEvent;
    private String metadataId;
    private String appName;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAppEvent(AppEvent appEvent) {
        this.appEvent = appEvent;
    }

    public AppEvent getAppEvent() {
        return appEvent;
    }

    public void setMetadataId(String metadataId) {
        this.metadataId = metadataId;
    }

    public String getMetadataId() {
        return metadataId;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppName() {
        return appName;
    }
}
