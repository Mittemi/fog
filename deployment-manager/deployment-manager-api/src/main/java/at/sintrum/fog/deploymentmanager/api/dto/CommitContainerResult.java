package at.sintrum.fog.deploymentmanager.api.dto;

/**
 * Created by Michael Mittermayr on 24.05.2017.
 */
public class CommitContainerResult {

    public CommitContainerResult(String id) {
        this.id = id;
    }

    public CommitContainerResult() {
    }

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
