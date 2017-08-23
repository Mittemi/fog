package at.sintrum.fog.applicationhousing.recovery.metadata;

import at.sintrum.fog.core.dto.FogIdentification;

/**
 * Created by Michael Mittermayr on 23.08.2017.
 */
public class FogCellMetadata extends RuntimeMetadataBase {

    private final FogIdentification fogIdentification;
    private final String profiles;

    public FogCellMetadata(String serviceId, FogIdentification fogIdentification, String profiles) {
        super(serviceId);
        this.fogIdentification = fogIdentification;
        this.profiles = profiles;
    }

    public String getProfiles() {
        return profiles;
    }

    public FogIdentification getFogIdentification() {
        return fogIdentification;
    }

    public boolean isCloud() {
        return profiles.contains("cloud");
    }
}