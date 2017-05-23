package at.sintrum.fog.deploymentmanager.api.dto;

import org.springframework.util.StringUtils;

/**
 * Created by Michael Mittermayr on 21.05.2017.
 */
public class VolumeInfo {

    private String hostDir;
    private String containerDir;

    public String getHostDir() {
        return hostDir;
    }

    public void setHostDir(String hostDir) {
        this.hostDir = hostDir;
    }

    public String getContainerDir() {
        return containerDir;
    }

    public void setContainerDir(String containerDir) {
        this.containerDir = containerDir;
    }
}
