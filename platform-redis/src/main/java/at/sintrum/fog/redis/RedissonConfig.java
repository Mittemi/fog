package at.sintrum.fog.redis;

import at.sintrum.fog.core.PlatformCoreConfig;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.DefaultCodecProvider;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * Created by Michael Mittermayr on 06.07.2017.
 */
@Configuration
@PropertySources(
        @PropertySource("classpath:redis-application.yml")
)
public class RedissonConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedissonConfig.class);

    @Bean
    public Config redissonConfig(@Value("${redis.host:localhost}") String host, @Value("${redis.port:6379}") int port) {

        String address = "redis://" + host + ":" + port;

        LOGGER.info("Enable redisson for server: " + address);

//        Config config = new Config();
//        config.setUseLinuxNativeEpoll(true);
//        config.useClusterServers()
//                // use "rediss://" for SSL connection
//                .addNodeAddress(address);

        Config config = new Config();
        config.useSingleServer().setAddress(address);
        config.setCodec(new JsonJacksonCodec(PlatformCoreConfig.createObjectMapper()));

        config.setCodecProvider(new DefaultCodecProvider());

        return config;
    }

    @Bean
    public RedissonClient redissonClient(Config config) {
        return Redisson.create(config);
    }
}
