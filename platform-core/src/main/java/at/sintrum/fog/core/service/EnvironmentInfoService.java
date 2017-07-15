package at.sintrum.fog.core.service;

/**
 * Created by Michael Mittermayr on 24.05.2017.
 */
public interface EnvironmentInfoService {

    String getOwnContainerId();

    boolean isInsideContainer();

    String getEurekaServiceUrl();

    String getEurekaClientIp();

    String getFogBaseUrl();

    String getFogId();

    String getOwnUrl();

    String getServiceProfile();
}
