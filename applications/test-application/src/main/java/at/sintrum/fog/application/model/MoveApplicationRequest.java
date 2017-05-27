package at.sintrum.fog.application.model;

/**
 * Created by Michael Mittermayr on 24.05.2017.
 */
public class MoveApplicationRequest {

    private String targetIp;

    private int targetPort;

    public String getTargetIp() {
        return targetIp;
    }

    public void setTargetIp(String targetIp) {
        this.targetIp = targetIp;
    }

    public int getTargetPort() {
        return targetPort;
    }

    public void setTargetPort(int targetPort) {
        this.targetPort = targetPort;
    }
}
