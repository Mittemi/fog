package at.sintrum.fog.simulation.model;

import java.util.Objects;

public class InstanceIdDetails {
    private String instanceId;

    private String name;

    public InstanceIdDetails() {
    }

    public InstanceIdDetails(String instanceId, String name) {
        this.instanceId = instanceId;
        this.name = name;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstanceIdDetails that = (InstanceIdDetails) o;
        return Objects.equals(instanceId, that.instanceId) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instanceId, name);
    }
}
