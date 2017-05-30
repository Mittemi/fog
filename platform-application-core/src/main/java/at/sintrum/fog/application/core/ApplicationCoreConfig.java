package at.sintrum.fog.application.core;

import at.sintrum.fog.core.PlatformCoreConfig;
import at.sintrum.fog.deploymentmanager.client.DeploymentManagerClientConfig;
import at.sintrum.fog.hostinfo.HostInfoProviderConfig;
import at.sintrum.fog.metadatamanager.client.MetadataManagerClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;

/**
 * Created by Michael Mittermayr on 24.05.2017.
 */
@Configuration
@Import({DeploymentManagerClientConfig.class, MetadataManagerClientConfig.class, PlatformCoreConfig.class, HostInfoProviderConfig.class})
@EnableDiscoveryClient
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
}
