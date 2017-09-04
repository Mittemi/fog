package at.sintrum.fog.application.client.factory;

import at.sintrum.fog.application.client.api.AppLifecycleClient;
import at.sintrum.fog.application.client.api.ApplicationInfoClient;
import at.sintrum.fog.clientcore.annotation.EnableRetry;
import at.sintrum.fog.clientcore.client.ClientFactory;

/**
 * Created by Michael Mittermayr on 23.08.2017.
 */
public interface ApplicationClientFactory extends ClientFactory {

    @EnableRetry
    ApplicationInfoClient createApplicationInfoClient(String url);

    @EnableRetry
    AppLifecycleClient createAppLifecycleClient(String url);
}
