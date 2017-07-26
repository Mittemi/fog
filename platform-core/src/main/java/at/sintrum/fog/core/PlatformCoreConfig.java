package at.sintrum.fog.core;

import at.sintrum.fog.core.config.FogApplicationConfigProperties;
import at.sintrum.fog.core.service.EnvironmentInfoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Michael Mittermayr on 16.05.2017.
 */
@Configuration
@ComponentScan(basePackageClasses = {EnvironmentInfoService.class})
@EnableConfigurationProperties({FogApplicationConfigProperties.class})
public class PlatformCoreConfig {

    private static final Logger LOG = LoggerFactory.getLogger(PlatformCoreConfig.class);

    @Bean
    public ObjectMapper objectMapper() {
        return createObjectMapper();
    }

    public static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JodaModule());
        objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        objectMapper.configure(com.fasterxml.jackson.databind.SerializationFeature.
//                WRITE_DATES_AS_TIMESTAMPS, false);
        //objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return objectMapper;
    }

}
