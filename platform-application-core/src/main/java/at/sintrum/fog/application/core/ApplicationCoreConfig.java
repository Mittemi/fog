package at.sintrum.fog.application.core;

import at.sintrum.fog.application.core.api.ApplicationInfo;
import at.sintrum.fog.application.core.api.RequestAppController;
import at.sintrum.fog.application.core.service.MoveApplicationService;
import at.sintrum.fog.application.core.service.MoveApplicationServiceImpl;
import at.sintrum.fog.applicationhousing.client.ApplicationHousingClientConfig;
import at.sintrum.fog.core.PlatformCoreConfig;
import at.sintrum.fog.core.service.EnvironmentInfoService;
import at.sintrum.fog.deploymentmanager.client.DeploymentManagerClientConfig;
import at.sintrum.fog.deploymentmanager.client.api.ApplicationManager;
import at.sintrum.fog.deploymentmanager.client.factory.DeploymentManagerClientFactory;
import at.sintrum.fog.hostinfo.HostInfoProviderConfig;
import at.sintrum.fog.metadatamanager.client.MetadataManagerClientConfig;
import at.sintrum.fog.redis.RedissonConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

/**
 * Created by Michael Mittermayr on 24.05.2017.
 */
@Configuration
@ComponentScan(basePackageClasses = {ApplicationInfo.class, RequestAppController.class})
@Import({PlatformCoreConfig.class, HostInfoProviderConfig.class, ApplicationHousingClientConfig.class, DeploymentManagerClientConfig.class, MetadataManagerClientConfig.class, RedissonConfig.class})
@EnableDiscoveryClient
@EnableScheduling
public class ApplicationCoreConfig {

    @Value("${EUREKA_SERVICE_URL:UNKNOWN}")
    private String eurekaUrl;

    @Value("${EUREKA_CLIENT_IP:UNKNOWN}")
    private String eurekaClientIp;

    @PostConstruct
    public void invoke() {
        Logger logger = LoggerFactory.getLogger(ApplicationCoreConfig.class);

        if ("UNKNOWN".equals(eurekaUrl) || "UNKNOWN".equals(eurekaClientIp)) {
            logger.warn("ENV settings for Eureka missing!");
        }

        logger.info("EurekaUrl: " + eurekaUrl);
        logger.info("EurekaClientIP: " + eurekaClientIp);
    }

    @Bean
    public MoveApplicationService moveApplicationService(EnvironmentInfoService environmentInfoService, DeploymentManagerClientFactory deploymentManagerClientFactory) {
        return new MoveApplicationServiceImpl(environmentInfoService, deploymentManagerClientFactory);
    }

    @Bean
    public ApplicationManager applicationManager(DeploymentManagerClientFactory deploymentManagerClientFactory, EnvironmentInfoService environmentInfoService) {
        return deploymentManagerClientFactory.createApplicationManagerClient(environmentInfoService.getFogBaseUrl());
    }
}
