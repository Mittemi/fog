package at.sintrum.fog.application.client;

import at.sintrum.fog.application.core.api.ApplicationInfoApi;
import at.sintrum.fog.application.core.api.RequestAppApi;
import at.sintrum.fog.clientcore.annotation.EnableRetry;
import at.sintrum.fog.clientcore.client.ClientFactory;

/**
 * Created by Michael Mittermayr on 23.08.2017.
 */
public interface ApplicationClientFactory extends ClientFactory {

    @EnableRetry
    ApplicationInfoApi createApplicationInfoClient(String url);

    @EnableRetry
    RequestAppApi createRequestAppClient(String url);
}
