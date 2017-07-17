package at.sintrum.fog.applicationhousing.api.dto;

/**
 * Created by Michael Mittermayr on 17.07.2017.
 */
public class AppUpdateMetadata {

    private AppIdentification current;

    private AppIdentification updated;

    public AppUpdateMetadata() {
    }

    public AppUpdateMetadata(AppIdentification current, AppIdentification updated) {
        this.current = current;
        this.updated = updated;
    }

    public AppIdentification getCurrent() {
        return current;
    }

    public void setCurrent(AppIdentification current) {
        this.current = current;
    }

    public AppIdentification getUpdated() {
        return updated;
    }

    public void setUpdated(AppIdentification updated) {
        this.updated = updated;
    }
}
