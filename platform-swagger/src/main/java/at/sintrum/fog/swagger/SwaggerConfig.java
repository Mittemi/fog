package at.sintrum.fog.swagger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by Michael Mittermayr on 17.05.2017.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {

        Logger logger = LoggerFactory.getLogger(SwaggerConfig.class);
        logger.info("Init Swagger");

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(this::listEndpoint)
                .paths(PathSelectors.any())
                .build();
    }

    private boolean listEndpoint(RequestHandler x) {
        if (x == null) {
            return false;
        }
        String packageName = x.declaringClass().getPackage().getName();
        if (packageName.startsWith("at.sintrum")) {
            return true;
        } else if (packageName.startsWith("org.springframework.boot.actuate.endpoint.mvc")) {
            String name = x.declaringClass().getName();
            return name.contains("ShutdownMvcEndpoint");
        }

        return false;
    }
}
