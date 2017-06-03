package at.sintrum.fog.metadatamanager.api.dto;

import org.joda.time.DateTime;

/**
 * Created by Michael Mittermayr on 03.06.2017.
 */
public abstract class MetadataBase {

    private DateTime lastUpdate;
    private DateTime creationDate;

    public DateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(DateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public DateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(DateTime creationDate) {
        this.creationDate = creationDate;
    }
}
