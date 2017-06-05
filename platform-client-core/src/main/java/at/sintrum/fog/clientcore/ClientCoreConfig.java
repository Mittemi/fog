package at.sintrum.fog.clientcore;

import at.sintrum.fog.clientcore.annotation.DoNotRegister;
import at.sintrum.fog.clientcore.client.ClientFactoryFactory;
import at.sintrum.fog.clientcore.client.ClientProvider;
import at.sintrum.fog.clientcore.client.ClientProviderImpl;
import at.sintrum.fog.clientcore.client.FeignClientFactoryFactoryImpl;
import feign.Client;
import feign.Contract;
import feign.codec.Decoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.WebMvcRegistrations;
import org.springframework.boot.autoconfigure.web.WebMvcRegistrationsAdapter;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.feign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Created by Michael Mittermayr on 17.05.2017.
 */
@Configuration
@Import({FeignClientsConfiguration.class})
public class ClientCoreConfig {

    private Logger LOG = LoggerFactory.getLogger(ClientCoreConfig.class);

    @Bean
    @ConditionalOnMissingBean
    public ClientProviderImpl clientProvider(DiscoveryClient discoveryClient, Client eurekaEnabledClient) {
        ClientProviderImpl clientProvider = new ClientProviderImpl(discoveryClient, eurekaEnabledClient, new Client.Default(null, null));
        return clientProvider;
    }

    @Bean
    public ClientFactoryFactory feignClientFactoryFactory(ClientProvider clientProvider, feign.codec.Encoder encoder, Decoder decoder, Contract contract) {
        return new FeignClientFactoryFactoryImpl(clientProvider, decoder, contract, encoder);
    }

    @Bean
    public WebMvcRegistrations feignWebRegistrations() {
        return new WebMvcRegistrationsAdapter() {
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                return new FeignFilterRequestMappingHandlerMapping();
            }
        };
    }

    private static class FeignFilterRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

        private static final Logger LOG = LoggerFactory.getLogger(FeignFilterRequestMappingHandlerMapping.class);

        @Override
        protected boolean isHandler(Class<?> beanType) {
            if (super.isHandler(beanType)) {
                boolean result = !AnnotatedElementUtils.hasAnnotation(beanType, FeignClient.class) && !AnnotatedElementUtils.hasAnnotation(beanType, DoNotRegister.class);

                if (result) {
                    if (beanType.getName().startsWith("com.sun.proxy")) {
                        LOG.info("Skip registration for proxy: " + beanType);
                        return false;
                    }
                    LOG.info("Register " + beanType);
                } else {
                    LOG.debug("Skip " + beanType);
                }
                return result;
            }
            return false;
        }
    }
}
