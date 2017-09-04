package at.sintrum.fog.deploymentmanager.client.factory;

import at.sintrum.fog.clientcore.client.ClientFactory;
import at.sintrum.fog.deploymentmanager.client.api.ApplicationManagerClient;
import at.sintrum.fog.deploymentmanager.client.api.ContainerManagerClient;
import at.sintrum.fog.deploymentmanager.client.api.ImageManagerClient;

/**
 * Created by Michael Mittermayr on 24.05.2017.
 */
public interface DeploymentManagerClientFactory extends ClientFactory {

    ImageManagerClient createImageManagerClient(String url);

    ContainerManagerClient createContainerManagerClient(String url);

    ApplicationManagerClient createApplicationManagerClient(String url);
}
