package at.sintrum.fog.clientcore.client;

import at.sintrum.fog.core.dto.FogIdentification;

/**
 * Created by Michael Mittermayr on 01.06.2017.
 */
public interface ClientFactory {

    String buildUrl(String ip, int port);

    String buildUrl(FogIdentification fogIdentification);
}
