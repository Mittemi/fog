package at.sintrum.fog.applicationhousing;

import at.sintrum.fog.application.client.ApplicationClientConfig;
import at.sintrum.fog.core.PlatformCoreConfig;
import at.sintrum.fog.deploymentmanager.client.DeploymentManagerClientConfig;
import at.sintrum.fog.metadatamanager.client.MetadataManagerClientConfig;
import at.sintrum.fog.redis.RedissonConfig;
import at.sintrum.fog.servercore.ServerCoreConfig;
import at.sintrum.fog.servercore.connectionfilter.SimulationControlledReachableServiceConfig;
import at.sintrum.fog.simulation.client.SimulationClientConfig;
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
@Import({
        RedissonConfig.class,
        SwaggerConfig.class,
        PlatformCoreConfig.class,
        ServerCoreConfig.class,
        MetadataManagerClientConfig.class,
        DeploymentManagerClientConfig.class,
        ApplicationClientConfig.class,
        SimulationClientConfig.class,
        SimulationControlledReachableServiceConfig.class
})
@EnableScheduling
public class AppHousingApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppHousingApplication.class, args);
    }
}

