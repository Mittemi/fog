package at.sintrum.fog.core;

import at.sintrum.fog.core.service.EnvironmentInfoService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Michael Mittermayr on 16.05.2017.
 */
@Configuration
@ComponentScan(basePackageClasses = {EnvironmentInfoService.class})
public class PlatformCoreConfig {

}
