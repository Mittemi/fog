package at.sintrum.fog.core.service;

import at.sintrum.fog.core.config.FogApplicationConfigProperties;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by Michael Mittermayr on 24.05.2017.
 */
@Service
public class EnvironmentInfoServiceImpl implements EnvironmentInfoService {

    private final org.slf4j.Logger LOG = LoggerFactory.getLogger(EnvironmentInfoServiceImpl.class);
    private final FogApplicationConfigProperties fogApplicationConfigProperties;
    private final String fogBaseUrl;
    private final String eurekaServiceUrl;
    private final String eurekaClientIP;
    private final String serverPort;
    private final String applicationName;
    private final String serviceProfile;
    private final String metadataId;
    private final Environment environment;

    public EnvironmentInfoServiceImpl(FogApplicationConfigProperties fogApplicationConfigProperties,
                                      @Value("${FOG_BASE_URL:UNKNOWN}") String fogBaseUrl,
                                      @Value("${EUREKA_SERVICE_URL:UNKNOWN}") String eurekaServiceUrl,
                                      @Value("${EUREKA_CLIENT_IP:UNKNOWN}") String eurekaClientIP,
                                      @Value("${server.port}") String serverPort,
                                      @Value("${spring.application.name}") String applicationName,
                                      @Value("${SERVICE_PROFILE:UNKNOWN}") String serviceProfile,
                                      @Value("${METADATA_ID:UNKNOWN}") String metadataId,
                                      Environment environment) {
        this.fogApplicationConfigProperties = fogApplicationConfigProperties;
        this.eurekaServiceUrl = eurekaServiceUrl;
        this.eurekaClientIP = eurekaClientIP;
        this.serverPort = serverPort;
        this.applicationName = applicationName;
        this.serviceProfile = serviceProfile;
        this.metadataId = metadataId;
        this.environment = environment;

        String activeProfiles = String.join(", ", environment.getActiveProfiles());
        LOG.info("Active Profiles: " + activeProfiles);


        if ("deployment-manager".equals(applicationName) && "UNKNOWN".equals(fogBaseUrl)) {
            fogBaseUrl = "http://" + eurekaClientIP + ":" + serverPort;
            LOG.info("FOG_BASE_URL, rewrite to: " + fogBaseUrl);
        } else {
            LOG.info("FOG_BASE_URL: " + fogBaseUrl);
        }
        this.fogBaseUrl = fogBaseUrl;
    }

    @Override
    public String getOwnContainerId() {
        return getHostname();
    }

    @Override
    public boolean isInsideContainer() {
        if (isWin()) {
            return false;
        }

        if (isLinux()) {
            File f = new File("/.dockerenv");
            return f.exists();
        }

        LOG.warn("Unable to determine if application runs inside a container!");
        return false;
    }

    public String getOwnUrl() {
        return "http://" + getEurekaClientIp() + ":" + serverPort;
    }

    @Override
    public String getServiceProfile() {
        return serviceProfile;
    }

    @Override
    public String getMetadataId() {
        return null;
    }

    @Override
    public boolean isCloud() {
        return getServiceProfile().contains("cloud");
    }

    @Override
    public String getEurekaServiceUrl() {
        return eurekaServiceUrl;
    }

    @Override
    public String getEurekaClientIp() {
        return eurekaClientIP;
    }

    @Override
    public String getFogBaseUrl() {
        return fogBaseUrl;
    }

    @Override
    public String getFogId() {
        return fogBaseUrl;      //TODO: check if this could get trimmed
    }

    private String getHostname() {
        if (isWin()) {
            return System.getenv("COMPUTERNAME");
            //return execReadToString("hostname");
        } else if (isLinux()) {
            return System.getenv("HOSTNAME");
            //return execReadToString("hostname");
            //return execReadToString("cat /etc/hostname");
        }
        LOG.warn("Unable to determine the hostname!");
        return null;
    }

    private boolean isLinux() {
        String OS = System.getProperty("os.name").toLowerCase();
        return OS.contains("nix") || OS.contains("nux");
    }

    private boolean isWin() {
        String OS = System.getProperty("os.name").toLowerCase();
        return OS.contains("win");
    }

    private String execReadToString(String execCommand) {
        try {
            Process proc = Runtime.getRuntime().exec(execCommand);
            try (InputStream stream = proc.getInputStream()) {
                try (Scanner s = new Scanner(stream).useDelimiter("\\A")) {
                    return s.hasNext() ? s.next() : "";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
