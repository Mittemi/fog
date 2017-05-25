package at.sintrum.fog.hostinfo;

import at.sintrum.fog.hostinfo.api.HostInfoController;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Michael Mittermayr on 24.05.2017.
 */
@Configuration
@ComponentScan(basePackageClasses = {HostInfoController.class})
public class HostInfoProviderConfig {
}
