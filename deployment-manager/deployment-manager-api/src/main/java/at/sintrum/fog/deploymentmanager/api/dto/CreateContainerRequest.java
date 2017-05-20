package at.sintrum.fog.deploymentmanager.api.dto;

import java.util.List;

/**
 * Created by Michael Mittermayr on 20.05.2017.
 */
public class CreateContainerRequest {

    private String image;

    private List<PortInfo> portInfos;

    private List<String> environment;

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
}
