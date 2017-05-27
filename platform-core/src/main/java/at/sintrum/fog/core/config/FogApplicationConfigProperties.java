package at.sintrum.fog.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Michael Mittermayr on 27.05.2017.
 */
@ConfigurationProperties(prefix = "fog.app")
public class FogApplicationConfigProperties {

    private String deploymentManagerUrl;

    public String getDeploymentManagerUrl() {
        return deploymentManagerUrl;
    }

    public void setDeploymentManagerUrl(String deploymentManagerUrl) {
        this.deploymentManagerUrl = deploymentManagerUrl;
    }
}
