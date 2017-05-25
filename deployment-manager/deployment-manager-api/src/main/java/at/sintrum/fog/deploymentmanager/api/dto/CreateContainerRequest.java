package at.sintrum.fog.deploymentmanager.api.dto;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Mittermayr on 20.05.2017.
 */
public class CreateContainerRequest {

    private String image;

    private List<PortInfo> portInfos = new LinkedList<>();

    private List<String> environment = new LinkedList<>();

    private List<VolumeInfo> volumes = new LinkedList<>();

    private String restartPolicy;

    private boolean withTty;

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

    public List<String> getEnvironment() {
        return environment;
    }

    public void setEnvironment(List<String> environment) {
        this.environment = environment;
    }

    public boolean isWithTty() {
        return withTty;
    }

    public void setWithTty(boolean withTty) {
        this.withTty = withTty;
    }

    public String getRestartPolicy() {
        return restartPolicy;
    }

    public void setRestartPolicy(String restartPolicy) {
        this.restartPolicy = restartPolicy;
    }

    public List<VolumeInfo> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<VolumeInfo> volumes) {
        this.volumes = volumes;
    }
}
