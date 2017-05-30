package at.sintrum.fog.metadatamanager.client.factory.impl;

import at.sintrum.fog.clientcore.client.ClientProvider;
import at.sintrum.fog.clientcore.client.FeignClientFactoryBase;
import at.sintrum.fog.metadatamanager.client.api.ApplicationMetadata;
import at.sintrum.fog.metadatamanager.client.factory.MetadataManagerClientFactory;

import feign.Contract;
import feign.codec.Decoder;
import feign.codec.Encoder;

/**
 * Created by Michael Mittermayr on 25.05.2017.
 */
public class FeignMetadataManagerClientFactory extends FeignClientFactoryBase implements MetadataManagerClientFactory {

    public FeignMetadataManagerClientFactory(ClientProvider clientProvider, Decoder decoder, Encoder encoder, Contract contract) {
        super(clientProvider, decoder, contract, encoder);
    }

    @Override
    public ApplicationMetadata createApplicationMetadata(String url) {
        return buildClient(ApplicationMetadata.class, url);
    }
}
