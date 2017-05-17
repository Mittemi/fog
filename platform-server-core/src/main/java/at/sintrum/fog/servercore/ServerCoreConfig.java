package at.sintrum.fog.servercore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Created by Michael Mittermayr on 17.05.2017.
 */
@Configuration
@EnableDiscoveryClient
public class ServerCoreConfig {

    @Value("${EUREKA_SERVICE_URL:UNKNOWN}")
    private String eurekaUrl;

    @Value("${EUREKA_CLIENT_IP:UNKNOWN}")
    private String eurekaClientIp;

    @PostConstruct
    public void invoke() {
        Logger logger = LoggerFactory.getLogger(ServerCoreConfig.class);

        if ("UNKNOWN".equals(eurekaUrl) || "UNKNOWN".equals(eurekaClientIp)) {
            logger.warn("ENV settings for Eureka missing!");
        }

        logger.info("EurekaUrl: " + eurekaUrl);
        logger.info("EurekaClientIP: " + eurekaClientIp);
    }

}
