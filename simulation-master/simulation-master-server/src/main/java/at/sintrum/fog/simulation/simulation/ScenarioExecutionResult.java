package at.sintrum.fog.simulation.simulation;

import at.sintrum.fog.simulation.scenario.dto.BasicScenarioInfo;
import org.joda.time.DateTime;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Mittermayr on 09.09.2017.
 */
public class ScenarioExecutionResult {

    private DateTime start;

    private DateTime end;

    private String scenarioName;

    private BasicScenarioInfo scenarioInfo;

    private List<AppExecutionLogging> appResults;

    public List<AppExecutionLogging> getAppResults() {
        return appResults;
    }

    public void setAppResults(List<AppExecutionLogging> appResults) {
        this.appResults = appResults;
    }

    public ScenarioExecutionResult() {
    }

    public ScenarioExecutionResult(String scenarioName, BasicScenarioInfo scenarioInfo, DateTime start) {
        this.start = start;
        this.scenarioName = scenarioName;
        this.scenarioInfo = scenarioInfo;
        appResults = new LinkedList<>();
    }

    public DateTime getStart() {
        return start;
    }

    public void setStart(DateTime start) {
        this.start = start;
    }

    public DateTime getEnd() {
        return end;
    }

    public void setEnd(DateTime end) {
        this.end = end;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    public BasicScenarioInfo getScenarioInfo() {
        return scenarioInfo;
    }

    public void setScenarioInfo(BasicScenarioInfo scenarioInfo) {
        this.scenarioInfo = scenarioInfo;
    }
}
