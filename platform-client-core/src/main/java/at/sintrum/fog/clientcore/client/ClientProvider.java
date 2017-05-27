package at.sintrum.fog.clientcore.client;

import feign.Client;

/**
 * Created by Michael Mittermayr on 27.05.2017.
 */
public interface ClientProvider {
    Client getClient(String url);
}
