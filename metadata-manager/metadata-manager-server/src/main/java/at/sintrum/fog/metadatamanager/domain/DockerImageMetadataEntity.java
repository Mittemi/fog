package at.sintrum.fog.metadatamanager.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

/**
 * Created by Michael Mittermayr on 30.05.2017.
 */
@RedisHash("imageMetadata")
public class DockerImageMetadataEntity extends BaseEntity {

    @Id
    private String id;

    private String image;

    private String tag;

    private List<Integer> ports;

    private List<String> environment;

    private boolean isEurekaEnabled;

    private boolean isStateless;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isStateless() {
        return isStateless;
    }

    public void setStateless(boolean stateless) {
        isStateless = stateless;
    }
}
