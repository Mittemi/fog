package at.sintrum.fog.applicationhousing.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Michael Mittermayr on 24.08.2017.
 */
@ConfigurationProperties(prefix = "fog.apphousing")
public class AppHousingConfigurationProperties {

    private int appStateMetadataGraceTimeout = 60;
    private int appRecoveryWaitTime = 60;
    private int appHeartbeatTimeout = 60;

    public int getAppStateMetadataGraceTimeout() {
        return appStateMetadataGraceTimeout;
    }

    public void setAppStateMetadataGraceTimeout(int appStateMetadataGraceTimeout) {
        this.appStateMetadataGraceTimeout = appStateMetadataGraceTimeout;
    }

    public int getAppRecoveryWaitTime() {
        return appRecoveryWaitTime;
    }

    public void setAppRecoveryWaitTime(int appRecoveryWaitTime) {
        this.appRecoveryWaitTime = appRecoveryWaitTime;
    }

    public int getAppHeartbeatTimeout() {
        return appHeartbeatTimeout;
    }

    public void setAppHeartbeatTimeout(int appHeartbeatTimeout) {
        this.appHeartbeatTimeout = appHeartbeatTimeout;
    }
}
