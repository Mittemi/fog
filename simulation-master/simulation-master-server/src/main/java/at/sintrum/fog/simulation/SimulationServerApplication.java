package at.sintrum.fog.simulation;

import at.sintrum.fog.application.client.ApplicationClientConfig;
import at.sintrum.fog.application.client.TestApplicationClientConfig;
import at.sintrum.fog.applicationhousing.client.ApplicationHousingClientConfig;
import at.sintrum.fog.core.PlatformCoreConfig;
import at.sintrum.fog.deploymentmanager.client.DeploymentManagerClientConfig;
import at.sintrum.fog.metadatamanager.client.MetadataManagerClientConfig;
import at.sintrum.fog.redis.RedissonConfig;
import at.sintrum.fog.servercore.ServerCoreConfig;
import at.sintrum.fog.swagger.SwaggerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Michael Mittermayr on 17.07.2017.
 */
@Configuration
@SpringBootApplication
@Import({
        RedissonConfig.class,
        SwaggerConfig.class,
        PlatformCoreConfig.class,
        ServerCoreConfig.class,

        DeploymentManagerClientConfig.class,
        MetadataManagerClientConfig.class,
        ApplicationHousingClientConfig.class,
        ApplicationClientConfig.class,
        TestApplicationClientConfig.class
})
public class SimulationServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimulationServerApplication.class, args);
    }
}
