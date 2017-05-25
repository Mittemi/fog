package at.sintrum.fog.clientcore;

import at.sintrum.fog.clientcore.annotation.DoNotRegister;
import org.springframework.boot.autoconfigure.web.WebMvcRegistrations;
import org.springframework.boot.autoconfigure.web.WebMvcRegistrationsAdapter;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.annotation.Annotation;

/**
 * Created by Michael Mittermayr on 17.05.2017.
 */
@Configuration
public class ClientCoreConfig {

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
