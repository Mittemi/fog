package at.sintrum.fog.metadatamanager.client.factory;

import at.sintrum.fog.clientcore.annotation.EnableRetry;
import at.sintrum.fog.clientcore.client.ClientFactory;
import at.sintrum.fog.metadatamanager.client.api.AppRequestClient;
import at.sintrum.fog.metadatamanager.client.api.ApplicationStateMetadataClient;
import at.sintrum.fog.metadatamanager.client.api.ContainerMetadataClient;
import at.sintrum.fog.metadatamanager.client.api.ImageMetadataClient;

/**
 * Created by Michael Mittermayr on 30.05.2017.
 */
public interface MetadataManagerClientFactory extends ClientFactory {

    @EnableRetry
    ImageMetadataClient createApplicationMetadataClient(String url);

    @EnableRetry
    ContainerMetadataClient createContainerMetadataClient(String url);

    @EnableRetry
    ApplicationStateMetadataClient createApplicationStateMetadataClient(String url);

    AppRequestClient createAppRequestClient(String url);
}