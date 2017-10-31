package at.sintrum.fog.application.service;

import at.sintrum.fog.core.PlatformCoreConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Michael Mittermayr on 12.10.2017.
 */
@Configuration
@Import({
        PlatformCoreConfig.class
})
public class TestConfig {
}
