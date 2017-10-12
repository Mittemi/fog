package at.sintrum.fog.application.core.model;

import at.sintrum.fog.core.dto.FogIdentification;

/**
 * Created by Michael Mittermayr on 12.10.2017.
 */
public class AppRequestInfo {

    private FogIdentification target;

    public AppRequestInfo(FogIdentification target) {
        this.target = target;
    }

    public AppRequestInfo() {
    }

    public FogIdentification getTarget() {
        return target;
    }

    public void setTarget(FogIdentification target) {
        this.target = target;
    }
}
