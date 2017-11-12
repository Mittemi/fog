package at.sintrum.fog.core.dto;

/**
 * Created by Michael Mittermayr on 21.08.2017.
 */
public class ResourceInfo {

    private int storage;
    private int cpu;
    private int memory;
    private int network;

    public ResourceInfo() {
    }

    public ResourceInfo(int storage, int cpu, int memory, int network) {
        this.storage = storage;
        this.cpu = cpu;
        this.memory = memory;
        this.network = network;
    }

    public ResourceInfo(ResourceInfo resourceInfo) {
        this.storage = resourceInfo.storage;
        this.cpu = resourceInfo.cpu;
        this.network = resourceInfo.network;
        this.memory = resourceInfo.memory;
    }

    public int getStorage() {
        return storage;
    }

    public void setStorage(int storage) {
        this.storage = storage;
    }

    public int getCpu() {
        return cpu;
    }

    public void setCpu(int cpu) {
        this.cpu = cpu;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public int getNetwork() {
        return network;
    }

    public void setNetwork(int network) {
        this.network = network;
    }

    public boolean isEnough(ResourceInfo resourceInfo) {
        return this.storage >= resourceInfo.storage && this.cpu >= resourceInfo.cpu && this.memory >= resourceInfo.memory && this.network >= resourceInfo.memory;
    }

    public ResourceInfo subtract(ResourceInfo resourceInfo) {
        this.storage -= resourceInfo.storage;
        this.cpu -= resourceInfo.cpu;
        this.memory -= resourceInfo.memory;
        this.network -= resourceInfo.network;
        return this;
    }

    public ResourceInfo copy() {
        return new ResourceInfo(this);
    }

    public ResourceInfo add(ResourceInfo resourceInfo) {
        this.storage += resourceInfo.storage;
        this.cpu += resourceInfo.cpu;
        this.memory += resourceInfo.memory;
        this.network += resourceInfo.network;
        return this;
    }

    public static ResourceInfo fixedSized(int numberApps) {
        return new ResourceInfo(numberApps, numberApps, numberApps, numberApps);
    }

    public void setToFixedSize(int value) {
        this.storage = value;
        this.cpu = value;
        this.memory = value;
        this.network = value;
    }
}
