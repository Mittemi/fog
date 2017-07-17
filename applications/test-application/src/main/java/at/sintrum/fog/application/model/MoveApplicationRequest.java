package at.sintrum.fog.application.model;

import at.sintrum.fog.core.dto.FogIdentification;

/**
 * Created by Michael Mittermayr on 24.05.2017.
 */
public class MoveApplicationRequest {

    private FogIdentification target;

    public FogIdentification getTarget() {
        return target;
    }

    public void setTarget(FogIdentification target) {
        this.target = target;
    }
}
