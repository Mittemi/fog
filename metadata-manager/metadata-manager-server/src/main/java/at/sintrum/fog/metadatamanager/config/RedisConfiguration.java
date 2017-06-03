package at.sintrum.fog.metadatamanager.config;

import at.sintrum.fog.metadatamanager.repository.ImageMetadataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

/**
 * Created by Michael Mittermayr on 30.05.2017.
 */
@Configuration
@EnableRedisRepositories(basePackageClasses = ImageMetadataRepository.class)
public class RedisConfiguration {

    private final Logger LOGGER = LoggerFactory.getLogger(RedisConfiguration.class);

    @Bean
    RedisConnectionFactory connectionFactory(@Value("${redis.host:localhost}") String redisHost, @Value("${redis.port:6379}") int port) {
        LOGGER.info("Redis-Server: " + redisHost + ":" + port);
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(redisHost);
        jedisConnectionFactory.setPort(port);
        return jedisConnectionFactory;
    }

    @Bean
    RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory connectionFactory) {

        RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        return template;
    }
}
