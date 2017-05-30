package at.sintrum.fog.metadatamanager.api.dto;

import java.util.List;

/**
 * Created by Michael Mittermayr on 30.05.2017.
 */
public class DockerImageMetadata {

    private String id;

    private List<Integer> ports;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Integer> getPorts() {
        return ports;
    }

    public void setPorts(List<Integer> ports) {
        this.ports = ports;
    }
}
