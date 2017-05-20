package at.sintrum.fog.deploymentmanager.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Mittermayr on 20.05.2017.
 */
@ConfigurationProperties(prefix = "fog.deploymentmanager")
public class DeploymentManagerConfigProperties {

    private List<String> protectedContainers = new LinkedList<>();

    public List<String> getProtectedContainers() {
        return protectedContainers;
    }

    public void setProtectedContainers(List<String> protectedContainers) {
        this.protectedContainers = protectedContainers;
    }
}
