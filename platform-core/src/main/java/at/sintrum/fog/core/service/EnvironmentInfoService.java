package at.sintrum.fog.core.service;

/**
 * Created by Michael Mittermayr on 24.05.2017.
 */
public interface EnvironmentInfoService {

    String getOwnContainerId();

    boolean isInsideContainer();

    String getEurekaServiceUrl();

    String getEurekaClientIp();

    int getPort();

    String getFogBaseUrl();

    String getFogId();

    String getOwnUrl();

    String getServiceProfile();

    boolean hasServiceProfile(String profile);

    String getMetadataId();

    boolean isCloud();

    String getApplicationName();

    String getInstanceId();
}
