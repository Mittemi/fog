package at.sintrum.fog.core;

import at.sintrum.fog.core.config.FogApplicationConfigProperties;
import at.sintrum.fog.core.service.EnvironmentInfoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
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

    public void configure() {
        
    }

    @Bean
    public ObjectMapper objectMapper() {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JodaModule());

        //objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return objectMapper;
    }

}
