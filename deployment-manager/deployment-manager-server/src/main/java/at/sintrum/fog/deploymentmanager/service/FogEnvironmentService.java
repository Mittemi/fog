package at.sintrum.fog.deploymentmanager.service;

/**
 * Created by Michael Mittermayr on 31.05.2017.
 */
public interface FogEnvironmentService {

    String getEurekaServiceUrl();

    String getEurekaClientIp();

    String getFogBaseUrl();
}
