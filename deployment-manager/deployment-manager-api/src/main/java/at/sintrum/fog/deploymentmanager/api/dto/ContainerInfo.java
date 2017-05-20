package at.sintrum.fog.deploymentmanager.api.dto;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Michael Mittermayr on 17.05.2017.
 */
public class ContainerInfo {

    public ContainerInfo() {
    }

    public ContainerInfo(String id, String imageId, String image, boolean running) {
        this.id = id;
        this.imageId = imageId;
        this.image = image;
        this.running = running;
    }

    private String id;

    private String imageId;

    private String image;

    private boolean running;

    private List<PortInfo> portInfos = new LinkedList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<PortInfo> getPortInfos() {
        return portInfos;
    }

    public void setPortInfos(List<PortInfo> portInfos) {
        this.portInfos = portInfos;
    }

    @Override
    public String toString() {
        return "Container: " + getId() + ", Image: " + getImage() + "(" + getImageId() + "), Ports: [ " + String.join(", ", getPortInfos().stream().map(PortInfo::toString).collect(Collectors.toList())) + " ]";
    }
}
