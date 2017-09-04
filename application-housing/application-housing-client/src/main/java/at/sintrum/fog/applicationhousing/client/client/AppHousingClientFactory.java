package at.sintrum.fog.applicationhousing.client.client;

import at.sintrum.fog.applicationhousing.client.api.AppEvolution;
import at.sintrum.fog.applicationhousing.client.api.AppRecovery;
import at.sintrum.fog.clientcore.annotation.EnableRetry;
import at.sintrum.fog.clientcore.client.ClientFactory;

/**
 * Created by Michael Mittermayr on 14.07.2017.
 */
public interface AppHousingClientFactory extends ClientFactory {

    @EnableRetry
    AppEvolution createAppEvolutionClient(String url);

    @EnableRetry
    AppRecovery createRecoveryClient(String url);
}
