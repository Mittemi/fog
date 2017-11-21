package at.sintrum.fog.simulation.model;

/**
 * Created by Michael Mittermayr on 20.11.2017.
 */
public class EvaluationQuickInfo {

    private String id;
    private String notes;

    public EvaluationQuickInfo() {
    }

    public EvaluationQuickInfo(String id, String notes) {
        this.id = id;
        this.notes = notes;
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
