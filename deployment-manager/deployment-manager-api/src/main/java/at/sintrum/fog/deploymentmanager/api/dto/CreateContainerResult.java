package at.sintrum.fog.deploymentmanager.api.dto;

/**
 * Created by Michael Mittermayr on 20.05.2017.
 */
public class CreateContainerResult {

    private String id;

    private String[] warnings;

    public CreateContainerResult(String id, String[] warnings) {
        this.id = id;
        this.warnings = warnings;
    }

    public CreateContainerResult() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getWarnings() {
        return warnings;
    }

    public void setWarnings(String[] warnings) {
        this.warnings = warnings;
    }
}
