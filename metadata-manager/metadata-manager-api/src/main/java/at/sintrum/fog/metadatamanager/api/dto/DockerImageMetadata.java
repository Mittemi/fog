package at.sintrum.fog.metadatamanager.api.dto;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by Michael Mittermayr on 30.05.2017.
 */
public class DockerImageMetadata {

    private String id;

    private String image;

    private String tag;

    private boolean isEurekaEnabled;

    private List<Integer> ports;

    private List<String> environment;

    private DateTime creationDate;

    private DateTime lastUpdate;

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

    public boolean isEurekaEnabled() {
        return isEurekaEnabled;
    }

    public void setEurekaEnabled(boolean eurekaEnabled) {
        isEurekaEnabled = eurekaEnabled;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public DateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(DateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public DateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(DateTime creationDate) {
        this.creationDate = creationDate;
    }
}
