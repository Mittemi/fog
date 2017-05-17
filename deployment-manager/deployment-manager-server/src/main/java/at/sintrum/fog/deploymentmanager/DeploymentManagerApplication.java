package at.sintrum.fog.deploymentmanager;

import at.sintrum.fog.core.PlatformCoreConfig;
import at.sintrum.fog.docker.DockerConfig;
import at.sintrum.fog.servercore.ServerCoreConfig;
import at.sintrum.fog.swagger.SwaggerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Michael Mittermayr on 17.05.2017.
 */
@Configuration
@EnableAutoConfiguration
@SpringBootApplication
@Import({DockerConfig.class, SwaggerConfig.class, PlatformCoreConfig.class, ServerCoreConfig.class})
public class DeploymentManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DeploymentManagerApplication.class, args);
    }
}
