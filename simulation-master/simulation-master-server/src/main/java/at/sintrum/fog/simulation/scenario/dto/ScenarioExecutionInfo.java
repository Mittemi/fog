package at.sintrum.fog.simulation.scenario.dto;

/**
 * Created by Michael Mittermayr on 02.09.2017.
 */
public class ScenarioExecutionInfo {

    private String name;

    public ScenarioExecutionInfo() {
    }

    public ScenarioExecutionInfo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
