package at.sintrum.fog.application.client.api;

import at.sintrum.fog.application.api.WorkApi;
import at.sintrum.fog.clientcore.client.ClientFactory;

/**
 * Created by Michael Mittermayr on 08.08.2017.
 */
public interface TestApplicationClientFactory extends ClientFactory {

    WorkApi createWorkClient(String url);
}
