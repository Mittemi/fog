package at.sintrum.fog.deploymentmanager.client.factory.impl;

import at.sintrum.fog.deploymentmanager.client.api.ContainerManager;
import at.sintrum.fog.deploymentmanager.client.api.ImageManager;
import at.sintrum.fog.deploymentmanager.client.factory.DeploymentManagerClientFactory;
import feign.Client;
import feign.Contract;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;

/**
 * Created by Michael Mittermayr on 25.05.2017.
 */
public class FeignDeploymentManagerClientFactory implements DeploymentManagerClientFactory {

    private final Decoder decoder;
    private final Encoder encoder;
    private final Client client;
    private final Contract contract;

    public FeignDeploymentManagerClientFactory(Decoder decoder, Encoder encoder, Client client, Contract contract) {
        this.decoder = decoder;
        this.encoder = encoder;
        this.client = client;
        this.contract = contract;
    }

    @Override
    public ImageManager createImageManagerClient(String url) {
        return buildClient(ImageManager.class, url);
    }

    @Override
    public ContainerManager createContainerManagerClient(String url) {
        return buildClient(ContainerManager.class, url);
    }

    private <T> T buildClient(Class<T> clazz, String url) {
        return Feign.builder().client(client).contract(contract)
                .encoder(encoder)
                .decoder(decoder)
                .target(clazz, url);
    }
}
