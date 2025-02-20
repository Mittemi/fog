package at.sintrum.fog.deploymentmanager.api.dto;

/**
 * Created by Michael Mittermayr on 20.05.2017.
 */
public class PushImageRequest {

    public PushImageRequest() {
    }

    public PushImageRequest(String name, String tag) {
        this.name = name;
        this.tag = tag;
    }

    private String name;

    private String tag;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
