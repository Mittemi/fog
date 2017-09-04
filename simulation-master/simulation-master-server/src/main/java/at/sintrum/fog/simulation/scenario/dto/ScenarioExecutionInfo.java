package at.sintrum.fog.simulation.scenario.dto;

/**
 * Created by Michael Mittermayr on 02.09.2017.
 */
public class ScenarioExecutionInfo {

    private String name;

    private boolean isFinished;

    public ScenarioExecutionInfo() {
    }

    public ScenarioExecutionInfo(String name, boolean isFinished) {
        this.name = name;
        this.isFinished = isFinished;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }
}
