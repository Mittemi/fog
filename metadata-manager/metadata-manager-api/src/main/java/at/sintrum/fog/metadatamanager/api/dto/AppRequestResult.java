package at.sintrum.fog.metadatamanager.api.dto;

/**
 * Created by Michael Mittermayr on 28.10.2017.
 */
public class AppRequestResult {

    private String internalId;

    public AppRequestResult() {
    }

    public AppRequestResult(String internalId) {

        this.internalId = internalId;
    }

    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }
}
