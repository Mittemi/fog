package at.sintrum.fog.core.dto;

/**
 * Created by Michael Mittermayr on 17.07.2017.
 */
public class FogIdentification {

    private String ip;

    private int port;

    public FogIdentification() {
    }

    public FogIdentification(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
