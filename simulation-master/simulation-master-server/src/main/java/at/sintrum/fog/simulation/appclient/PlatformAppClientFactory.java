package at.sintrum.fog.simulation.appclient;

import at.sintrum.fog.clientcore.annotation.EnableRetry;
import at.sintrum.fog.clientcore.client.ClientFactory;

/**
 * Created by Michael Mittermayr on 08.08.2017.
 */
public interface PlatformAppClientFactory extends ClientFactory {

    @EnableRetry
    PlatformAppClient createPlatformAppClient(String url);
}
