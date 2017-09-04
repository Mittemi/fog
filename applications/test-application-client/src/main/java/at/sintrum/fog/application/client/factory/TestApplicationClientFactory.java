package at.sintrum.fog.application.client.factory;

import at.sintrum.fog.application.client.api.WorkClient;
import at.sintrum.fog.clientcore.client.ClientFactory;

/**
 * Created by Michael Mittermayr on 08.08.2017.
 */
public interface TestApplicationClientFactory extends ClientFactory {

    WorkClient createWorkClient(String url);
}
