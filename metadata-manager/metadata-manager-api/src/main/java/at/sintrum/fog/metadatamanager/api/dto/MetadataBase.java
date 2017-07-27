package at.sintrum.fog.metadatamanager.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.ser.DateTimeSerializer;
import org.joda.time.DateTime;

/**
 * Created by Michael Mittermayr on 03.06.2017.
 */
public abstract class MetadataBase {

    @JsonSerialize(using = DateTimeSerializer.class)
    private DateTime lastUpdate;
    @JsonSerialize(using = DateTimeSerializer.class)
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
