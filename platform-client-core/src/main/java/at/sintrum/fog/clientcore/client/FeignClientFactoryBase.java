package at.sintrum.fog.clientcore.client;

import feign.Contract;
import feign.Feign;
import feign.Logger;
import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.slf4j.Slf4jLogger;
import org.springframework.cloud.client.discovery.DiscoveryClient;

/**
 * Created by Michael Mittermayr on 27.05.2017.
 */
public class FeignClientFactoryBase {
    private ClientProvider clientProvider;
    private final Decoder decoder;
    private final Encoder encoder;
    private final Contract contract;

    public FeignClientFactoryBase(ClientProvider clientProvider, Decoder decoder, Contract contract, Encoder encoder) {
        this.clientProvider = clientProvider;
        this.decoder = decoder;
        this.contract = contract;
        this.encoder = encoder;
        DiscoveryClient d;
    }

    public String buildUrl(String ip, int port) {
        return "http://" + ip + ":" + port;
    }

    protected <T> T buildClient(Class<T> clazz, String url) {
        return Feign.builder()
                .client(clientProvider.getClient(url))
                .contract(contract)
                .encoder(encoder)
                .decoder(decoder)
                .logger(new Slf4jLogger(clazz.getName()))
                .logLevel(Logger.Level.FULL)
                .retryer(Retryer.NEVER_RETRY)       //TODO: retry?
                .decode404()
                .target(clazz, url);
    }
}
