package at.sintrum.fog.metadatamanager.client.factory;

import at.sintrum.fog.clientcore.annotation.EnableRetry;
import at.sintrum.fog.clientcore.client.ClientFactory;
import at.sintrum.fog.metadatamanager.api.ApplicationStateMetadataApi;
import at.sintrum.fog.metadatamanager.api.ContainerMetadataApi;
import at.sintrum.fog.metadatamanager.api.ImageMetadataApi;

/**
 * Created by Michael Mittermayr on 30.05.2017.
 */
public interface MetadataManagerClientFactory extends ClientFactory {

    @EnableRetry
    ImageMetadataApi createApplicationMetadataClient(String url);

    @EnableRetry
    ContainerMetadataApi createContainerMetadataClient(String url);

    @EnableRetry
    ApplicationStateMetadataApi createApplicationStateMetadataClient(String url);
}
