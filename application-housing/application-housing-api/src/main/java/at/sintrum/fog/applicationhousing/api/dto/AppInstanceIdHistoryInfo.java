package at.sintrum.fog.applicationhousing.api.dto;

/**
 * Created by Michael Mittermayr on 02.09.2017.
 */
public class AppInstanceIdHistoryInfo {

    private String oldInstanceId;

    private String newInstanceId;

    public AppInstanceIdHistoryInfo() {
    }

    public AppInstanceIdHistoryInfo(String oldInstanceId, String newInstanceId) {
        this.oldInstanceId = oldInstanceId;
        this.newInstanceId = newInstanceId;
    }

    public String getOldInstanceId() {
        return oldInstanceId;
    }

    public void setOldInstanceId(String oldInstanceId) {
        this.oldInstanceId = oldInstanceId;
    }

    public String getNewInstanceId() {
        return newInstanceId;
    }

    public void setNewInstanceId(String newInstanceId) {
        this.newInstanceId = newInstanceId;
    }
}
