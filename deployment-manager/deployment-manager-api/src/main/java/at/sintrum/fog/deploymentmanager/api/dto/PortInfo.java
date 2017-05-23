package at.sintrum.fog.deploymentmanager.api.dto;

/**
 * Created by Michael Mittermayr on 20.05.2017.
 */
public class PortInfo {

    private String ip;
    private Integer containerPort;
    private Integer hostPort;
    private String type;

    public PortInfo() {
    }

    public PortInfo(String ip, Integer containerPort, Integer hostPort, String type) {
        this.ip = ip;
        this.containerPort = containerPort;
        this.hostPort = hostPort;
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getContainerPort() {
        return containerPort;
    }

    public void setContainerPort(Integer containerPort) {
        this.containerPort = containerPort;
    }

    public Integer getHostPort() {
        return hostPort;
    }

    public void setHostPort(Integer hostPort) {
        this.hostPort = hostPort;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return ip + ":" + hostPort + "->" + containerPort + "(" + type + ")";
    }
}
