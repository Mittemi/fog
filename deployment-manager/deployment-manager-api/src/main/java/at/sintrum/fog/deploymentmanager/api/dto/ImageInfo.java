package at.sintrum.fog.deploymentmanager.api.dto;

import java.util.List;

/**
 * Created by Michael Mittermayr on 20.05.2017.
 */
public class ImageInfo {

    public ImageInfo(String id, List<String> tags) {
        this.id = id;
        this.tags = tags;
    }

    private String id;

    private List<String> tags;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
