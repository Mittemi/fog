package at.sintrum.fog.simulation;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Michael Mittermayr on 31.10.2017.
 */
@ConfigurationProperties(prefix = "fog.simulation")
public class SimulationServerConfig {

    private String registryUrl;

    public String getRegistryUrl() {
        return registryUrl;
    }

    public void setRegistryUrl(String registryUrl) {
        this.registryUrl = registryUrl;
    }
}
