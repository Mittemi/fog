package at.sintrum.fog.swagger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        List<Parameter> parameters = Stream.of("FogId", "FogBaseUrl", "ContainerId")
                .map(x -> new ParameterBuilder()
                        .name(x)
                        .allowMultiple(false)
                        .required(false)
                        .modelRef(new ModelRef("string"))
                        .parameterType("header")
                        .build()).collect(Collectors.toList());

        return new Docket(DocumentationType.SWAGGER_2)
                .globalOperationParameters(parameters)
                .select()
                .apis(this::listEndpoint)
                .paths(PathSelectors.any())
                .build();
    }

    private boolean listEndpoint(RequestHandler x) {
        if (x == null) {
            return false;
        }
        Class<?> aClass = x.declaringClass();
        if (aClass == null) {
            return false;
        }

        Package aPackage = aClass.getPackage();
        if (aPackage == null) {
            return false;
        }

        String packageName = aPackage.getName();
        if (packageName.startsWith("at.sintrum")) {
            return true;
        } else if (packageName.startsWith("org.springframework.boot.actuate.endpoint.mvc")) {
            String name = aClass.getName();
            return name.contains("ShutdownMvcEndpoint");
        }

        return false;
    }
}
