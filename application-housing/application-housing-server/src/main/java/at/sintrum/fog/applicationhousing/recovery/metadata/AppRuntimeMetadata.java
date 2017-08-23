package at.sintrum.fog.applicationhousing.recovery.metadata;

import org.joda.time.DateTime;
import org.springframework.util.StringUtils;

/**
 * Created by Michael Mittermayr on 23.08.2017.
 */
public class AppRuntimeMetadata extends RuntimeMetadataBase {

    private String instanceId;
    private boolean retired;

    private DateTime lastRecoveryCall;

    public AppRuntimeMetadata(String serviceId, String instanceId) {
        super(serviceId);
        this.instanceId = instanceId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public boolean isIgnored() {
        return StringUtils.isEmpty(instanceId);
    }

    public boolean isRetired() {
        return retired;
    }

    public void setRetired(boolean retired) {
        this.retired = retired;
    }

    public DateTime getLastRecoveryCall() {
        return lastRecoveryCall;
    }

    public void setLastRecoveryCall(DateTime lastRecoveryCall) {
        this.lastRecoveryCall = lastRecoveryCall;
    }
}
