package at.sintrum.fog.deploymentmanager.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by Michael Mittermayr on 31.05.2017.
 */
@Service
public class FogEnvironmentServiceImpl implements FogEnvironmentService {

    private final String eurekaServiceUrl;
    private final String eurekaClientIP;
    private String serverPort;

    //TODO: replace server-port by eureka non-secure-port
    public FogEnvironmentServiceImpl(@Value("${EUREKA_SERVICE_URL:UNKNOWN}") String eurekaServiceUrl, @Value("${EUREKA_CLIENT_IP:UNKNOWN}") String eurekaClientIP, @Value("${server.port}") String serverPort) {
        this.eurekaServiceUrl = eurekaServiceUrl;
        this.eurekaClientIP = eurekaClientIP;
        this.serverPort = serverPort;
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
        return "http://" + eurekaClientIP + ":" + serverPort;
    }
}
