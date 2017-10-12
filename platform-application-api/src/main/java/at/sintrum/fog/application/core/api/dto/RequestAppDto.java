package at.sintrum.fog.application.core.api.dto;

import at.sintrum.fog.core.dto.FogIdentification;

/**
 * Created by Michael Mittermayr on 12.10.2017.
 */
public class RequestAppDto {

    private FogIdentification target;

    public RequestAppDto() {
    }

    public RequestAppDto(FogIdentification target) {
        this.target = target;
    }

    public FogIdentification getTarget() {
        return target;
    }

    public void setTarget(FogIdentification target) {
        this.target = target;
    }
}
