package at.sintrum.fog.clientcore.client;

/**
 * Created by Michael Mittermayr on 01.06.2017.
 */
public interface ClientFactoryFactory extends ClientFactory {
    <T extends ClientFactory> T buildFactory(Class<T> factoryInterface, String serviceName);

    <T> T buildClient(Class<T> apiInterface, String url);
}
