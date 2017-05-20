package at.sintrum.fog.deploymentmanager.api.dto;

/**
 * Created by Michael Mittermayr on 20.05.2017.
 */
public class EnvironmentSetting {

    private String key;
    private String value;

    public EnvironmentSetting(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public EnvironmentSetting() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "EnvironmentSetting{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
