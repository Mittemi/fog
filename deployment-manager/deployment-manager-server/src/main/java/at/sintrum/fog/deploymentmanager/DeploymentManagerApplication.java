package at.sintrum.fog.deploymentmanager;

import at.sintrum.fog.applicationhousing.client.ApplicationHousingClientConfig;
import at.sintrum.fog.core.PlatformCoreConfig;
import at.sintrum.fog.deploymentmanager.client.DeploymentManagerClientConfig;
import at.sintrum.fog.deploymentmanager.config.DeploymentManagerConfigProperties;
import at.sintrum.fog.docker.DockerConfig;
import at.sintrum.fog.metadatamanager.client.MetadataManagerClientConfig;
import at.sintrum.fog.redis.RedissonConfig;
import at.sintrum.fog.servercore.ServerCoreConfig;
import at.sintrum.fog.simulation.client.SimulationClientConfig;
import at.sintrum.fog.swagger.SwaggerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Michael Mittermayr on 17.05.2017.
 */
@Configuration
@EnableAutoConfiguration
@SpringBootApplication
@EnableConfigurationProperties(DeploymentManagerConfigProperties.class)
@Import({DockerConfig.class,
        RedissonConfig.class,
        SwaggerConfig.class,
        PlatformCoreConfig.class,
        ServerCoreConfig.class,
        ApplicationHousingClientConfig.class,
        MetadataManagerClientConfig.class,
        DeploymentManagerClientConfig.class,
        SimulationClientConfig.class})
public class DeploymentManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DeploymentManagerApplication.class, args);
    }
}
