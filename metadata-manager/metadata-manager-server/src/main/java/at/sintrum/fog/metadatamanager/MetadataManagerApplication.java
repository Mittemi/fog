package at.sintrum.fog.metadatamanager;

import at.sintrum.fog.core.PlatformCoreConfig;
import at.sintrum.fog.metadatamanager.config.MetadataManagerConfigProperties;
import at.sintrum.fog.metadatamanager.config.RedisConfiguration;
import at.sintrum.fog.servercore.ServerCoreConfig;
import at.sintrum.fog.swagger.SwaggerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Michael Mittermayr on 30.05.2017.
 */
@Configuration
@EnableAutoConfiguration
@SpringBootApplication
@EnableConfigurationProperties(MetadataManagerConfigProperties.class)
@Import({SwaggerConfig.class, PlatformCoreConfig.class, ServerCoreConfig.class, RedisConfiguration.class})
public class MetadataManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MetadataManagerApplication.class, args);
    }
}
