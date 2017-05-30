package factory;

import factory.api.ApplicationMetadata;

/**
 * Created by Michael Mittermayr on 30.05.2017.
 */
public interface MetadataManagerClientFactory {

    String buildUrl(String ip, int port);

    ApplicationMetadata createApplicationMetadata(String url);
}
