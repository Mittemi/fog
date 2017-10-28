package at.sintrum.fog.metadatamanager.api.dto;

/**
 * Created by Michael Mittermayr on 28.10.2017.
 */
public class AppRequestDto {

    private AppRequest appRequest;

    private int credits;

    private String internalId;

    public AppRequestDto() {
    }

    public AppRequestDto(AppRequest appRequest, int credits, String internalId) {
        this.appRequest = appRequest;
        this.credits = credits;
        this.internalId = internalId;
    }

    public AppRequest getAppRequest() {
        return appRequest;
    }

    public void setAppRequest(AppRequest appRequest) {
        this.appRequest = appRequest;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }
}
