package at.sintrum.fog.clientcore.client;

import feign.Contract;
import feign.Feign;
import feign.Logger;
import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.slf4j.Slf4jLogger;
import org.springframework.util.StringUtils;

import java.lang.reflect.Proxy;

/**
 * Created by Michael Mittermayr on 27.05.2017.
 */
public class FeignClientFactoryFactoryImpl implements ClientFactoryFactory {
    private ClientProvider clientProvider;
    private final Decoder decoder;
    private final Encoder encoder;
    private final Contract contract;

    public FeignClientFactoryFactoryImpl(ClientProvider clientProvider, Decoder decoder, Contract contract, Encoder encoder) {
        this.clientProvider = clientProvider;
        this.decoder = decoder;
        this.contract = contract;
        this.encoder = encoder;
    }

    @Override
    public String buildUrl(String ip, int port) {
        return "http://" + ip + ":" + port;
    }

    public <T> T buildClient(Class<T> apiInterface, String url) {
        return Feign.builder()
                .client(clientProvider.getClient(url))
                .contract(contract)
                .encoder(encoder)
                .decoder(decoder)
                .logger(new Slf4jLogger(apiInterface.getName()))
                .logLevel(Logger.Level.FULL)
                .retryer(Retryer.NEVER_RETRY)       //TODO: retry?
                //.decode404()
                .target(apiInterface, url);
    }

    private String fixUrl(String url, String serviceName) {
        if (StringUtils.isEmpty(url)) {
            return "http://" + serviceName;
        }
        return url;
    }

    @Override
    public <T extends ClientFactory> T buildFactory(Class<T> factoryInterface, String serviceName) {
        return factoryInterface.cast(Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{factoryInterface}, (proxy, method, args) -> {

            if (method.getName().equals("buildUrl")) {
                return buildUrl((String) args[0], (Integer) args[1]);
            }

            String url = null;
            if (args.length > 0) {
                url = (String) args[0];
            }

            url = fixUrl(url, serviceName);

            return buildClient(method.getReturnType(), url);
        }));
    }
}
