package at.sintrum.fog.simulation.simulation;

import at.sintrum.fog.simulation.scenario.dto.BasicScenarioInfo;
import org.joda.time.DateTime;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Michael Mittermayr on 09.09.2017.
 */
public class ScenarioExecutionResult {

    private String executionId;

    private DateTime start;

    private DateTime end;

    private String scenarioName;

    private BasicScenarioInfo scenarioInfo;

    private List<AppExecutionLogging> appResults;

    private boolean useAuctioning;

    public List<AppExecutionLogging> getAppResults() {
        return appResults;
    }

    public void setAppResults(List<AppExecutionLogging> appResults) {
        this.appResults = appResults;
    }

    public ScenarioExecutionResult(String scenarioName, BasicScenarioInfo scenarioInfo, DateTime start, boolean useAuctioning) {
        this.start = start;
        this.scenarioName = scenarioName;
        this.scenarioInfo = scenarioInfo;
        this.useAuctioning = useAuctioning;
        appResults = new LinkedList<>();
        executionId = UUID.randomUUID().toString();
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

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public synchronized AppExecutionLogging addOrGetAppExecutionLogging(String imageMetadataId) {

        AppExecutionLogging appExecutionLogging = getAppResults().stream().filter(x -> x.getImageMetadataId().equals(imageMetadataId)).findFirst().orElse(null);
        if (appExecutionLogging == null) {
            appExecutionLogging = new AppExecutionLogging(imageMetadataId);
            getAppResults().add(appExecutionLogging);
            return appExecutionLogging;
        }
        return appExecutionLogging;
    }

    public boolean isUseAuctioning() {
        return useAuctioning;
    }

    public void setUseAuctioning(boolean useAuctioning) {
        this.useAuctioning = useAuctioning;
    }
}
