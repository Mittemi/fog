package at.sintrum.fog.application.core;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Michael Mittermayr on 09.08.2017.
 */
@ConfigurationProperties(prefix = "fog.app")
public class FogAppConfigProperties {

    private boolean enableSimulationMode;

    public boolean isEnableSimulationMode() {
        return enableSimulationMode;
    }

    public void setEnableSimulationMode(boolean enableSimulationMode) {
        this.enableSimulationMode = enableSimulationMode;
    }
}
