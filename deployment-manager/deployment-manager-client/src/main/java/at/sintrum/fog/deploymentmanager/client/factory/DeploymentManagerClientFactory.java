package at.sintrum.fog.deploymentmanager.client.factory;

import at.sintrum.fog.clientcore.client.ClientFactory;
import at.sintrum.fog.deploymentmanager.client.api.ApplicationManager;
import at.sintrum.fog.deploymentmanager.client.api.ContainerManager;
import at.sintrum.fog.deploymentmanager.client.api.ImageManager;

/**
 * Created by Michael Mittermayr on 24.05.2017.
 */
public interface DeploymentManagerClientFactory extends ClientFactory {

    ImageManager createImageManagerClient(String url);

    ContainerManager createContainerManagerClient(String url);

    ApplicationManager createApplicationManagerClient(String url);
}
