package at.sintrum.fog.applicationhousing.api.dto;

/**
 * Created by Michael Mittermayr on 15.07.2017.
 */
public class AppUpdateInfo {

    private boolean updateRequired;

    public boolean isUpdateRequired() {
        return updateRequired;
    }

    public void setUpdateRequired(boolean updateRequired) {
        this.updateRequired = updateRequired;
    }
}
