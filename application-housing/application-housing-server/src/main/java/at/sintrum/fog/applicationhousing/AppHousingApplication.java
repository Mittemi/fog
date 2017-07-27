package at.sintrum.fog.applicationhousing;

import at.sintrum.fog.core.PlatformCoreConfig;
import at.sintrum.fog.deploymentmanager.client.DeploymentManagerClientConfig;
import at.sintrum.fog.metadatamanager.client.MetadataManagerClientConfig;
import at.sintrum.fog.redis.RedissonConfig;
import at.sintrum.fog.servercore.ServerCoreConfig;
import at.sintrum.fog.swagger.SwaggerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by Michael Mittermayr on 14.07.2017.
 */
@Configuration
@EnableAutoConfiguration
@SpringBootApplication
@Import({RedissonConfig.class, SwaggerConfig.class, PlatformCoreConfig.class, ServerCoreConfig.class, MetadataManagerClientConfig.class, DeploymentManagerClientConfig.class})
@EnableScheduling
public class AppHousingApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppHousingApplication.class, args);
    }
}

