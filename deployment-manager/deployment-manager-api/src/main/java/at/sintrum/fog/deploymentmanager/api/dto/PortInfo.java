package at.sintrum.fog.deploymentmanager.api.dto;

/**
 * Created by Michael Mittermayr on 20.05.2017.
 */
public class PortInfo {

    private String ip;
    private Integer privatePort;
    private Integer publicPort;
    private String type;

    public PortInfo() {
    }

    public PortInfo(String ip, Integer privatePort, Integer publicPort, String type) {
        this.ip = ip;
        this.privatePort = privatePort;
        this.publicPort = publicPort;
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPrivatePort() {
        return privatePort;
    }

    public void setPrivatePort(Integer privatePort) {
        this.privatePort = privatePort;
    }

    public Integer getPublicPort() {
        return publicPort;
    }

    public void setPublicPort(Integer publicPort) {
        this.publicPort = publicPort;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return ip + ":" + publicPort + "->" + privatePort + "(" + type + ")";
    }
}
