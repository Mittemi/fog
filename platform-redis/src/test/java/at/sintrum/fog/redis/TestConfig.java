package at.sintrum.fog.redis;

import at.sintrum.fog.core.PlatformCoreConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Michael Mittermayr on 26.07.2017.
 */
@Configuration
@Import({RedissonConfig.class, PlatformCoreConfig.class})
public class TestConfig {
}
