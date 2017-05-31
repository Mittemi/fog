package at.sintrum.fog.metadatamanager.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by Michael Mittermayr on 30.05.2017.
 */
@RedisHash("imageMetadata")
public class DockerImageMetadataEntity {

    @Id
    private String id;

    private String tag;

    private List<Integer> ports;

    private List<String> environment;

    private boolean isEurekaEnabled;

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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<String> getEnvironment() {
        return environment;
    }

    public void setEnvironment(List<String> environment) {
        this.environment = environment;
    }

    public boolean isEurekaEnabled() {
        return isEurekaEnabled;
    }

    public void setEurekaEnabled(boolean eurekaEnabled) {
        isEurekaEnabled = eurekaEnabled;
    }
}
