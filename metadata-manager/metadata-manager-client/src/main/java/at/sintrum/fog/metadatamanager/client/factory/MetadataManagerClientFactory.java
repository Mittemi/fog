package at.sintrum.fog.metadatamanager.client.factory;

import at.sintrum.fog.metadatamanager.client.api.ApplicationMetadata;

/**
 * Created by Michael Mittermayr on 30.05.2017.
 */
public interface MetadataManagerClientFactory {

    String buildUrl(String ip, int port);

    ApplicationMetadata createApplicationMetadata(String url);
}
