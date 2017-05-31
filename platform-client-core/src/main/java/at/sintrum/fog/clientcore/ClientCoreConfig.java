package at.sintrum.fog.clientcore;

import at.sintrum.fog.clientcore.annotation.DoNotRegister;
import at.sintrum.fog.clientcore.client.ClientProviderImpl;
import feign.Client;
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
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.annotation.Annotation;

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
    public WebMvcRegistrations feignWebRegistrations() {
        return new WebMvcRegistrationsAdapter() {
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                return new FeignFilterRequestMappingHandlerMapping();
            }
        };
    }

    private static class FeignFilterRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
        @Override
        protected boolean isHandler(Class<?> beanType) {
            return super.isHandler(beanType) && doesntHaveAnnotation(beanType, FeignClient.class) && doesntHaveAnnotation(beanType, DoNotRegister.class);
        }

        private boolean doesntHaveAnnotation(Class<?> beanType, Class<? extends Annotation> annotation) {
            return AnnotationUtils.findAnnotation(beanType, annotation) == null;
        }
    }
}
