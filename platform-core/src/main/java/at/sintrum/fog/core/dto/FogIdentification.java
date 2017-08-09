package at.sintrum.fog.core.dto;

import org.springframework.util.StringUtils;

import java.util.Objects;

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

    public static FogIdentification parseFogBaseUrl(String baseUrl) {

        if (StringUtils.isEmpty(baseUrl)) {
            return null;
        }

        if (baseUrl.startsWith("http://")) {
            baseUrl = baseUrl.substring("http://".length());
        }

        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }

        int idx = baseUrl.lastIndexOf(":");
        String ip = baseUrl.substring(0, idx);
        int port = Integer.parseInt(baseUrl.substring(idx + 1));

        return new FogIdentification(ip, port);
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

    public boolean isSameFog(FogIdentification identification) {
        return Objects.equals(this.ip, identification.ip) && Objects.equals(this.port, identification.port);
    }

    public String toFogId() {
        return ip + ":" + port;
    }

    public String toUrl() {
        return "http://" + ip + ":" + port;
    }
}
