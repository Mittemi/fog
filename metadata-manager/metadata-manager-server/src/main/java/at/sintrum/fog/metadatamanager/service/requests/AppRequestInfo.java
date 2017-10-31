package at.sintrum.fog.metadatamanager.service.requests;

import at.sintrum.fog.metadatamanager.api.dto.AppRequest;
import org.joda.time.DateTime;

import java.util.UUID;

/**
 * Created by Michael Mittermayr on 28.10.2017.
 */
public class AppRequestInfo {

    private AppRequest appRequest;

    private DateTime creationDate;

    private DateTime finishedDate;

    private String targetFog;

    private int credits;

    private String internalId;

    public AppRequest getAppRequest() {
        return appRequest;
    }

    public DateTime getCreationDate() {
        return creationDate;
    }

    public AppRequestInfo(AppRequest appRequest, int credits) {
        this.appRequest = appRequest;
        this.targetFog = appRequest.getTarget().toFogId();
        this.credits = credits;
        this.creationDate = new DateTime();
        this.internalId = UUID.randomUUID().toString();
    }

    public AppRequestInfo() {
    }

    public String getTargetFog() {
        return targetFog;
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

    public void setAppRequest(AppRequest appRequest) {
        this.appRequest = appRequest;
    }

    public void setCreationDate(DateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setTargetFog(String targetFog) {
        this.targetFog = targetFog;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    public void incrementCredits(int credits) {
        this.credits += credits;
    }

    public DateTime getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(DateTime finishedDate) {
        this.finishedDate = finishedDate;
    }
}
