package at.sintrum.fog.servercore.connectionfilter;

import at.sintrum.fog.core.dto.FogIdentification;

/**
 * Created by Michael Mittermayr on 30.08.2017.
 */
public interface ServiceStateInfoService {

    boolean isOnlineFor(FogIdentification fogIdentification);
}
