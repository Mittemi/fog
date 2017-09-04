package at.sintrum.fog.applicationhousing.client.client;

import at.sintrum.fog.applicationhousing.client.api.AppEvolutionClient;
import at.sintrum.fog.applicationhousing.client.api.AppRecoveryClient;
import at.sintrum.fog.clientcore.annotation.EnableRetry;
import at.sintrum.fog.clientcore.client.ClientFactory;

/**
 * Created by Michael Mittermayr on 14.07.2017.
 */
public interface AppHousingClientFactory extends ClientFactory {

    @EnableRetry
    AppEvolutionClient createAppEvolutionClient(String url);

    @EnableRetry
    AppRecoveryClient createRecoveryClient(String url);
}
