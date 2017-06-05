package at.sintrum.fog.deploymentmanager.api.dto;

/**
 * Created by Michael Mittermayr on 24.05.2017.
 */
public class CommitContainerResult {

    public CommitContainerResult(String id, String image) {
        this.id = id;
        this.image = image;
    }

    public CommitContainerResult() {
    }

    private String id;

    private String image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
