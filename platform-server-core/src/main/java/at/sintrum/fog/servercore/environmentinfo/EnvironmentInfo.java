package at.sintrum.fog.servercore.environmentinfo;

/**
 * Created by Michael Mittermayr on 26.07.2017.
 */
public class EnvironmentInfo {

    private String activeProfile;
    private String eurekaServiceUrl;
    private String eurekaClientIp;
    private String applicationName;
    private String containerId;
    private String fogId;
    private boolean isInsideContainer;
    private boolean isCloud;
    private String swaggerUrl;

    public String getActiveProfile() {
        return activeProfile;
    }

    public void setActiveProfile(String activeProfile) {
        this.activeProfile = activeProfile;
    }

    public void setEurekaServiceUrl(String eurekaServiceUrl) {
        this.eurekaServiceUrl = eurekaServiceUrl;
    }

    public String getEurekaServiceUrl() {
        return eurekaServiceUrl;
    }

    public void setEurekaClientIp(String eurekaClientIp) {
        this.eurekaClientIp = eurekaClientIp;
    }

    public String getEurekaClientIp() {
        return eurekaClientIp;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setFogId(String fogId) {
        this.fogId = fogId;
    }

    public String getFogId() {
        return fogId;
    }

    public void setIsInsideContainer(boolean isInsideContainer) {
        this.isInsideContainer = isInsideContainer;
    }

    public boolean isInsideContainer() {
        return isInsideContainer;
    }

    public void setInsideContainer(boolean isInsideContainer) {
        this.isInsideContainer = isInsideContainer;
    }

    public void setIsCloud(boolean isCloud) {
        this.isCloud = isCloud;
    }

    public boolean isCloud() {
        return isCloud;
    }

    public void setCloud(boolean isCloud) {
        this.isCloud = isCloud;
    }

    public void setSwaggerUrl(String swaggerUrl) {
        this.swaggerUrl = swaggerUrl;
    }

    public String getSwaggerUrl() {
        return swaggerUrl;
    }
}
