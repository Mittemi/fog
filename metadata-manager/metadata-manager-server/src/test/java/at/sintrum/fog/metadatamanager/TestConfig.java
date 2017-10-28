package at.sintrum.fog.metadatamanager;

import at.sintrum.fog.core.PlatformCoreConfig;
import at.sintrum.fog.metadatamanager.config.MetadataManagerConfigProperties;
import at.sintrum.fog.redis.RedissonConfig;
import at.sintrum.fog.servercore.ServerCoreConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Michael Mittermayr on 12.10.2017.
 */
@Configuration
@Import({
        PlatformCoreConfig.class,
        RedissonConfig.class,
        ServerCoreConfig.class
})
@EnableAutoConfiguration
@SpringBootApplication
@EnableConfigurationProperties(MetadataManagerConfigProperties.class)
public class TestConfig {
}
