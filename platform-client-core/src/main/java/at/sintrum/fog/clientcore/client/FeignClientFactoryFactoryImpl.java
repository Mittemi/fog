package at.sintrum.fog.clientcore.client;

import at.sintrum.fog.clientcore.annotation.EnableRetry;
import at.sintrum.fog.core.dto.FogIdentification;
import feign.Contract;
import feign.Feign;
import feign.Logger;
import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.slf4j.Slf4jLogger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Proxy;

/**
 * Created by Michael Mittermayr on 27.05.2017.
 */
public class FeignClientFactoryFactoryImpl implements ClientFactoryFactory {
    private ClientProvider clientProvider;
    private final Decoder decoder;
    private final Encoder encoder;
    private final FogRequestInterceptor fogRequestInterceptor;
    private final Contract contract;
    private final org.slf4j.Logger LOG = LoggerFactory.getLogger(FeignClientFactoryFactoryImpl.class);

    public FeignClientFactoryFactoryImpl(ClientProvider clientProvider, Decoder decoder, Contract contract, Encoder encoder, FogRequestInterceptor fogRequestInterceptor) {
        this.clientProvider = clientProvider;
        this.decoder = decoder;
        this.contract = contract;
        this.encoder = encoder;
        this.fogRequestInterceptor = fogRequestInterceptor;
    }

    @Override
    public String buildUrl(String ip, int port) {
        return "http://" + ip + ":" + port;
    }

    @Override
    public String buildUrl(FogIdentification fogIdentification) {
        return buildUrl(fogIdentification.getIp(), fogIdentification.getPort());
    }

    public <T> T buildClient(Class<T> apiInterface, String url) {
        return buildClient(apiInterface, url, false);
    }

    public <T> T buildClient(Class<T> apiInterface, String url, boolean enableRetry) {

        Retryer retryer = Retryer.NEVER_RETRY;
        if (enableRetry) {
            LOG.debug("Enable retry for: " + apiInterface.getCanonicalName());
            retryer = new Retryer.Default();
        }

        return Feign.builder()
                .client(clientProvider.getClient(url))
                .contract(contract)
                .encoder(encoder)
                .decoder(decoder)
                .requestInterceptor(fogRequestInterceptor)
                .logger(new Slf4jLogger(apiInterface.getName()))
                .logLevel(Logger.Level.FULL)
                .errorDecoder(new ErrorDecoder.Default())
                .retryer(retryer)
                //.decode404()
                .target(apiInterface, url);
    }

    private String fixUrl(String url, String serviceName) {
        if (StringUtils.isEmpty(url)) {
            if (StringUtils.isEmpty(serviceName)) {
                LOG.error("A url is required since this client doesn't support automated resolving.");
                throw new RuntimeException("This client doesn't support automated url resolving. Please provide a URL");
            }

            return "http://" + serviceName;
        }
        return url;
    }

    @Override
    public <T extends ClientFactory> T buildFactory(Class<T> factoryInterface, String serviceName) {
        return factoryInterface.cast(Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{factoryInterface}, (proxy, method, args) -> {

            if (method.getName().equals("buildUrl")) {
                if (args.length == 2) {
                    return buildUrl((String) args[0], (Integer) args[1]);
                } else if (args.length == 1) {
                    return buildUrl((FogIdentification) args[0]);
                } else {
                    throw new Exception("Invalid amount of arguments.");
                }
            }

            String url = null;
            if (args.length > 0) {
                url = (String) args[0];
            }

            url = fixUrl(url, serviceName);

            return buildClient(method.getReturnType(), url, AnnotatedElementUtils.hasAnnotation(method, EnableRetry.class));
        }));
    }
}
