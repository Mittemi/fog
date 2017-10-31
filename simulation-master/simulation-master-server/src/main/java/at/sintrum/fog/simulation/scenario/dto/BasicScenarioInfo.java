package at.sintrum.fog.simulation.scenario.dto;

import at.sintrum.fog.core.dto.FogIdentification;

/**
 * Created by Michael Mittermayr on 31.08.2017.
 */
public class BasicScenarioInfo {

    private FogIdentification cloud;

    private FogIdentification fogA;

    private int creditsA;

    private FogIdentification fogB;

    private int creditsB;

    private FogIdentification fogC;

    private int creditsC;

    private FogIdentification fogD;

    private int creditsD;

    private FogIdentification fogE;

    private int creditsE;

    private int iterations;

    private int secondsBetweenRequests;

    public FogIdentification getCloud() {
        return cloud;
    }

    public void setCloud(FogIdentification cloud) {
        this.cloud = cloud;
    }

    public FogIdentification getFogA() {
        return fogA;
    }

    public void setFogA(FogIdentification fogA) {
        this.fogA = fogA;
    }

    public FogIdentification getFogB() {
        return fogB;
    }

    public void setFogB(FogIdentification fogB) {
        this.fogB = fogB;
    }

    public FogIdentification getFogC() {
        return fogC;
    }

    public void setFogC(FogIdentification fogC) {
        this.fogC = fogC;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public FogIdentification getFogE() {
        return fogE;
    }

    public void setFogE(FogIdentification fogE) {
        this.fogE = fogE;
    }

    public FogIdentification getFogD() {
        return fogD;
    }

    public void setFogD(FogIdentification fogD) {
        this.fogD = fogD;
    }

    public int getCreditsA() {
        return creditsA;
    }

    public void setCreditsA(int creditsA) {
        this.creditsA = creditsA;
    }

    public int getCreditsB() {
        return creditsB;
    }

    public void setCreditsB(int creditsB) {
        this.creditsB = creditsB;
    }

    public int getCreditsC() {
        return creditsC;
    }

    public void setCreditsC(int creditsC) {
        this.creditsC = creditsC;
    }

    public int getCreditsD() {
        return creditsD;
    }

    public void setCreditsD(int creditsD) {
        this.creditsD = creditsD;
    }

    public int getCreditsE() {
        return creditsE;
    }

    public void setCreditsE(int creditsE) {
        this.creditsE = creditsE;
    }

    public int getSecondsBetweenRequests() {
        return secondsBetweenRequests;
    }

    public void setSecondsBetweenRequests(int secondsBetweenRequests) {
        this.secondsBetweenRequests = secondsBetweenRequests;
    }
}
