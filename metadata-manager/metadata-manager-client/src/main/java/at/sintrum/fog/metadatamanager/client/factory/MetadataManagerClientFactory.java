package at.sintrum.fog.metadatamanager.client.factory;

import at.sintrum.fog.clientcore.client.ClientFactory;
import at.sintrum.fog.metadatamanager.client.api.ApplicationMetadata;

/**
 * Created by Michael Mittermayr on 30.05.2017.
 */
public interface MetadataManagerClientFactory extends ClientFactory {

    ApplicationMetadata createApplicationMetadata(String url);
}
