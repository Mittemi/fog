package at.sintrum.fog.core.dto;

/**
 * Created by Michael Mittermayr on 21.08.2017.
 */
public class ResourceInfo {

    private float storage;
    private float cpu;
    private float memory;
    private float network;

    public ResourceInfo() {
    }

    public ResourceInfo(float storage, float cpu, float memory, float network) {
        this.storage = storage;
        this.cpu = cpu;
        this.memory = memory;
        this.network = network;
    }

    public float getStorage() {
        return storage;
    }

    public void setStorage(float storage) {
        this.storage = storage;
    }

    public float getCpu() {
        return cpu;
    }

    public void setCpu(float cpu) {
        this.cpu = cpu;
    }

    public float getMemory() {
        return memory;
    }

    public void setMemory(float memory) {
        this.memory = memory;
    }

    public float getNetwork() {
        return network;
    }

    public void setNetwork(float network) {
        this.network = network;
    }

    public boolean isEnough(ResourceInfo resourceInfo) {
        return this.storage >= resourceInfo.storage && this.cpu >= resourceInfo.cpu && this.memory >= resourceInfo.memory && this.network >= resourceInfo.memory;
    }

    public ResourceInfo subtract(ResourceInfo resourceInfo) {
        return new ResourceInfo(this.storage - resourceInfo.storage, this.cpu - resourceInfo.cpu, this.memory - resourceInfo.memory, this.network - resourceInfo.network);
    }
}
