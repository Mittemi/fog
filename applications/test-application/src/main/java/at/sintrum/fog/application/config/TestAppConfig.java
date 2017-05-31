package at.sintrum.fog.application.config;

import at.sintrum.fog.application.core.ApplicationCoreConfig;
import at.sintrum.fog.deploymentmanager.client.DeploymentManagerClientConfig;
import at.sintrum.fog.hostinfo.HostInfoProviderConfig;
import at.sintrum.fog.swagger.SwaggerConfig;
import feign.Feign;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.WebMvcRegistrations;
import org.springframework.boot.autoconfigure.web.WebMvcRegistrationsAdapter;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Created by Michael Mittermayr on 24.05.2017.
 */
@Configuration
@Import({ApplicationCoreConfig.class, SwaggerConfig.class})
public class TestAppConfig {

}
