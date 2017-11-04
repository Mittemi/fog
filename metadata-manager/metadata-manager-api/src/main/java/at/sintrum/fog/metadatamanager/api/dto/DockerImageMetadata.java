package at.sintrum.fog.metadatamanager.api.dto;

import java.util.List;

/**
 * Created by Michael Mittermayr on 30.05.2017.
 */
public class DockerImageMetadata extends MetadataBase {

    private String id;

    private String image;

    private String applicationName;

    private String tag;

    private boolean isEurekaEnabled;

    private boolean isAutocompleteWorkEnabled;

    private boolean isStateless;

    private boolean enableDebugging;

    private String baseImageId;

    private List<Integer> ports;

    private List<String> environment;

    private String appStorageDirectory;

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

    public String getAppStorageDirectory() {
        return appStorageDirectory;
    }

    public void setAppStorageDirectory(String appStorageDirectory) {
        this.appStorageDirectory = appStorageDirectory;
    }

    public String getBaseImageId() {
        return baseImageId;
    }

    public void setBaseImageId(String baseImageId) {
        this.baseImageId = baseImageId;
    }

    public boolean isEnableDebugging() {
        return enableDebugging;
    }

    public void setEnableDebugging(boolean enableDebugging) {
        this.enableDebugging = enableDebugging;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public boolean isAutocompleteWorkEnabled() {
        return isAutocompleteWorkEnabled;
    }

    public void setAutocompleteWorkEnabled(boolean autocompleteWorkEnabled) {
        isAutocompleteWorkEnabled = autocompleteWorkEnabled;
    }
}
