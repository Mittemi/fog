package at.sintrum.fog.simulation.api.dto;

import at.sintrum.fog.core.dto.FogIdentification;

/**
 * Created by Michael Mittermayr on 09.09.2017.
 */
public class AppEventInfo {

    private String imageMetadataId;
    private FogIdentification originalLocation;

    private FogIdentification newLocation;

    private String originalInstanceId;

    private String newInstanceId;

    private boolean successful;


    public AppEventInfo() {
    }

    public AppEventInfo(String imageMetadataId, FogIdentification originalLocation, FogIdentification newLocation, String originalInstanceId, String newInstanceId, boolean successful) {
        this.imageMetadataId = imageMetadataId;
        this.originalLocation = originalLocation;
        this.newLocation = newLocation;
        this.originalInstanceId = originalInstanceId;
        this.newInstanceId = newInstanceId;
        this.successful = successful;
    }

    public FogIdentification getOriginalLocation() {
        return originalLocation;
    }

    public void setOriginalLocation(FogIdentification originalLocation) {
        this.originalLocation = originalLocation;
    }

    public FogIdentification getNewLocation() {
        return newLocation;
    }

    public void setNewLocation(FogIdentification newLocation) {
        this.newLocation = newLocation;
    }

    public String getOriginalInstanceId() {
        return originalInstanceId;
    }

    public void setOriginalInstanceId(String originalInstanceId) {
        this.originalInstanceId = originalInstanceId;
    }

    public String getNewInstanceId() {
        return newInstanceId;
    }

    public void setNewInstanceId(String newInstanceId) {
        this.newInstanceId = newInstanceId;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public String getImageMetadataId() {
        return imageMetadataId;
    }

    public void setImageMetadataId(String imageMetadataId) {
        this.imageMetadataId = imageMetadataId;
    }
}
