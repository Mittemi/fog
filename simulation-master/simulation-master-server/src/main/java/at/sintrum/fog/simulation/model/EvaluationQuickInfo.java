package at.sintrum.fog.simulation.model;

/**
 * Created by Michael Mittermayr on 20.11.2017.
 */
public class EvaluationQuickInfo {

    private String id;
    private String notes;
    private String executionId;

    public EvaluationQuickInfo() {
    }

    public EvaluationQuickInfo(String id, String notes, String executionId) {
        this.id = id;
        this.notes = notes;
        this.executionId = executionId;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
