package at.sintrum.fog.deploymentmanager.api.dto;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Mittermayr on 24.05.2017.
 */
public class CommitContainerRequest {

    private String containerId;
    private List<String> tags = new LinkedList<>();

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
