package at.sintrum.fog.servercore.service;

/**
 * Created by Michael Mittermayr on 25.07.2017.
 */
public interface RequestInfoService {
    String getCallerFogId();

    String getCallerServiceUrl();
}
