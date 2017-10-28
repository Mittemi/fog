package at.sintrum.fog.metadatamanager.api.dto;

/**
 * Created by Michael Mittermayr on 28.10.2017.
 */
public class AppRequestResult {

    private String internalId;

    private int creditsTotal;

    public AppRequestResult() {
    }

    public AppRequestResult(String internalId, int creditsTotal) {

        this.internalId = internalId;
        this.creditsTotal = creditsTotal;
    }

    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    public int getCreditsTotal() {
        return creditsTotal;
    }

    public void setCreditsTotal(int creditsTotal) {
        this.creditsTotal = creditsTotal;
    }
}
