package at.sintrum.fog.metadatamanager.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

/**
 * Created by Michael Mittermayr on 30.05.2017.
 */
@RedisHash("imageMetadata")
public class DockerImageMetadataEntity {

    @Id
    private String id;

    private List<Integer> ports;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Integer> getPorts() {
        return ports;
    }

    public void setPorts(List<Integer> ports) {
        this.ports = ports;
    }
}
