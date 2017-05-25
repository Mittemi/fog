package at.sintrum.fog.hostinfo.dto;

/**
 * Created by Michael Mittermayr on 24.05.2017.
 */
public class HostInfoDto {

    private String containerId;

    private boolean insideContainer;

    public HostInfoDto() {
    }

    public HostInfoDto(String containerId, boolean insideContainer) {

        this.containerId = containerId;
        this.insideContainer = insideContainer;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public boolean isInsideContainer() {
        return insideContainer;
    }

    public void setInsideContainer(boolean insideContainer) {
        this.insideContainer = insideContainer;
    }
}
