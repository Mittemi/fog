package at.sintrum.fog.metadatamanager.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 * Created by Michael Mittermayr on 03.06.2017.
 */
@RedisHash("containerMetadata")
public class DockerContainerMetadataEntity extends BaseEntity {

    @Id
    private String containerId;

    private String imageMetadataId;

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getImageMetadataId() {
        return imageMetadataId;
    }

    public void setImageMetadataId(String imageMetadataId) {
        this.imageMetadataId = imageMetadataId;
    }
}
