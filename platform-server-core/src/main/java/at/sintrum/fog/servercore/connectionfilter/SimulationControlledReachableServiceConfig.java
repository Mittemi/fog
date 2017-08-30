package at.sintrum.fog.servercore.connectionfilter;

import at.sintrum.fog.servercore.ServerCoreConfig;
import at.sintrum.fog.servercore.service.RequestInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Michael Mittermayr on 30.08.2017.
 */
@Configuration
@ComponentScan(basePackageClasses = {ServiceStateInfoServiceImpl.class, ServiceConnectionInterceptor.class, RegisterInterceptorConfig.class})
@Import({ServerCoreConfig.class})
public class SimulationControlledReachableServiceConfig {

    private static final Logger LOG = LoggerFactory.getLogger(SimulationControlledReachableServiceConfig.class);

    @Bean
    public ServiceConnectionInterceptor serviceConnectionFilter(ServiceStateInfoService serviceStateInfoService, RequestInfoService requestInfoService) {
        LOG.debug("Simulation controlled service reachability enabled");
        return new ServiceConnectionInterceptor(serviceStateInfoService, requestInfoService);
    }
}
