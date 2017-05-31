package at.sintrum.fog.clientcore.client;

import feign.Contract;
import feign.Feign;
import feign.Logger;
import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.slf4j.Slf4jLogger;
import org.springframework.util.StringUtils;

/**
 * Created by Michael Mittermayr on 27.05.2017.
 */
public class FeignClientFactoryBase {
    private ClientProvider clientProvider;
    private final Decoder decoder;
    private final Encoder encoder;
    private final Contract contract;
    private final String serviceName;

    public FeignClientFactoryBase(ClientProvider clientProvider, Decoder decoder, Contract contract, Encoder encoder, String serviceName) {
        this.clientProvider = clientProvider;
        this.decoder = decoder;
        this.contract = contract;
        this.encoder = encoder;
        this.serviceName = serviceName;
    }

    public String buildUrl(String ip, int port) {
        return "http://" + ip + ":" + port;
    }

    protected <T> T buildClient(Class<T> clazz, String url) {

        url = buildUrl(url);

        return Feign.builder()
                .client(clientProvider.getClient(url))
                .contract(contract)
                .encoder(encoder)
                .decoder(decoder)
                .logger(new Slf4jLogger(clazz.getName()))
                .logLevel(Logger.Level.FULL)
                .retryer(Retryer.NEVER_RETRY)       //TODO: retry?
                //.decode404()
                .target(clazz, url);
    }

    private String buildUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            return "http://" + serviceName;
        }
        return url;
    }
}
