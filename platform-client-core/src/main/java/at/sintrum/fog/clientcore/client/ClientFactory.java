package at.sintrum.fog.clientcore.client;

/**
 * Created by Michael Mittermayr on 01.06.2017.
 */
public interface ClientFactory {

    String buildUrl(String ip, int port);
}
