package at.sintrum.fog.application.core;

import at.sintrum.fog.application.core.api.ApplicationInfo;
import at.sintrum.fog.application.core.api.RequestAppController;
import at.sintrum.fog.applicationhousing.client.ApplicationHousingClientConfig;
import at.sintrum.fog.core.PlatformCoreConfig;
import at.sintrum.fog.core.service.EnvironmentInfoService;
import at.sintrum.fog.deploymentmanager.client.DeploymentManagerClientConfig;
import at.sintrum.fog.deploymentmanager.client.api.ApplicationManager;
import at.sintrum.fog.deploymentmanager.client.factory.DeploymentManagerClientFactory;
import at.sintrum.fog.hostinfo.HostInfoProviderConfig;
import at.sintrum.fog.metadatamanager.client.MetadataManagerClientConfig;
import at.sintrum.fog.redis.RedissonConfig;
import at.sintrum.fog.simulation.client.SimulationClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

/**
 * Created by Michael Mittermayr on 24.05.2017.
 */
@Configuration
@ComponentScan(basePackageClasses = {ApplicationInfo.class, RequestAppController.class})
@Import({PlatformCoreConfig.class,
        HostInfoProviderConfig.class,
        ApplicationHousingClientConfig.class,
        DeploymentManagerClientConfig.class,
        MetadataManagerClientConfig.class,
        RedissonConfig.class,
        SimulationClientConfig.class
})
@EnableDiscoveryClient
@EnableScheduling
@EnableConfigurationProperties({FogAppConfigProperties.class})
@EnableAsync
public class ApplicationCoreConfig {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationCoreConfig.class);

    @Value("${EUREKA_SERVICE_URL:UNKNOWN}")
    private String eurekaUrl;

    @Value("${EUREKA_CLIENT_IP:UNKNOWN}")
    private String eurekaClientIp;

    @PostConstruct
    public void invoke() {
        if ("UNKNOWN".equals(eurekaUrl) || "UNKNOWN".equals(eurekaClientIp)) {
            LOG.warn("ENV settings for Eureka missing!");
        }

        LOG.info("EurekaUrl: " + eurekaUrl);
        LOG.info("EurekaClientIP: " + eurekaClientIp);
    }

    @Bean
    public ApplicationManager applicationManager(DeploymentManagerClientFactory deploymentManagerClientFactory, EnvironmentInfoService environmentInfoService) {
        return deploymentManagerClientFactory.createApplicationManagerClient(environmentInfoService.getFogBaseUrl());
    }
}
