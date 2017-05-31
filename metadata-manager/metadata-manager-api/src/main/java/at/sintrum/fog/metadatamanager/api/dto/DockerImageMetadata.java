package at.sintrum.fog.metadatamanager.api.dto;

import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by Michael Mittermayr on 30.05.2017.
 */
public class DockerImageMetadata {

    private String id;

    private String tag;

    private List<Integer> ports;

    private List<String> environment;

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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<String> getEnvironment() {
        return environment;
    }

    public void setEnvironment(List<String> environment) {
        this.environment = environment;
    }
}
