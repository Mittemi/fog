package at.sintrum.fog.metadatamanager.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Michael Mittermayr on 30.05.2017.
 */
@ConfigurationProperties(prefix = "fog.metadatamanager")
public class MetadataManagerConfigProperties {

    private boolean useAuction;

    public MetadataManagerConfigProperties(boolean useAuction) {
        this.useAuction = useAuction;
    }

    public MetadataManagerConfigProperties() {
    }

    public boolean isUseAuction() {
        return useAuction;
    }

    public void setUseAuction(boolean useAuction) {
        this.useAuction = useAuction;
    }
}
