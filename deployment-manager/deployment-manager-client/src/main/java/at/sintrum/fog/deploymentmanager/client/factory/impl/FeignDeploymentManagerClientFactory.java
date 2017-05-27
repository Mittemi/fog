package at.sintrum.fog.deploymentmanager.client.factory.impl;

import at.sintrum.fog.clientcore.client.ClientProvider;
import at.sintrum.fog.clientcore.client.FeignClientFactoryBase;
import at.sintrum.fog.deploymentmanager.client.api.ContainerManager;
import at.sintrum.fog.deploymentmanager.client.api.ImageManager;
import at.sintrum.fog.deploymentmanager.client.factory.DeploymentManagerClientFactory;
import feign.Contract;
import feign.codec.Decoder;
import feign.codec.Encoder;

/**
 * Created by Michael Mittermayr on 25.05.2017.
 */
public class FeignDeploymentManagerClientFactory extends FeignClientFactoryBase implements DeploymentManagerClientFactory {

    public FeignDeploymentManagerClientFactory(ClientProvider clientProvider, Decoder decoder, Encoder encoder, Contract contract) {
        super(clientProvider, decoder, contract, encoder);
    }

    @Override
    public ImageManager createImageManagerClient(String url) {
        return buildClient(ImageManager.class, url);
    }

    @Override
    public ContainerManager createContainerManagerClient(String url) {
        return buildClient(ContainerManager.class, url);
    }
}
