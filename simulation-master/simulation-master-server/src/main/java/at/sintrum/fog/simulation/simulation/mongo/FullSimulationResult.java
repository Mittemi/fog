package at.sintrum.fog.simulation.simulation.mongo;

import at.sintrum.fog.simulation.scenario.dto.ScenarioExecutionInfo;
import at.sintrum.fog.simulation.simulation.ScenarioExecutionResult;
import org.springframework.data.annotation.Id;

import java.util.Map;

/**
 * Created by Michael Mittermayr on 03.11.2017.
 */
public class FullSimulationResult {

    @Id
    private String id;

    private Map<String, String> instanceIdHistory;

    private ScenarioExecutionResult executionResult;

    private ScenarioExecutionInfo executionInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getInstanceIdHistory() {
        return instanceIdHistory;
    }

    public void setInstanceIdHistory(Map<String, String> instanceIdHistory) {
        this.instanceIdHistory = instanceIdHistory;
    }

    public ScenarioExecutionResult getExecutionResult() {
        return executionResult;
    }

    public void setExecutionResult(ScenarioExecutionResult executionResult) {
        this.executionResult = executionResult;
    }

    public ScenarioExecutionInfo getExecutionInfo() {
        return executionInfo;
    }

    public void setExecutionInfo(ScenarioExecutionInfo executionInfo) {
        this.executionInfo = executionInfo;
    }
}
