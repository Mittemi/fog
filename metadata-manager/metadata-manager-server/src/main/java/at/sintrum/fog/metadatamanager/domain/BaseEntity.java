package at.sintrum.fog.metadatamanager.domain;

import java.util.Date;

/**
 * Created by Michael Mittermayr on 02.06.2017.
 */
public class BaseEntity {

    private Date creationDate;

    private Date lastUpdate;

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
