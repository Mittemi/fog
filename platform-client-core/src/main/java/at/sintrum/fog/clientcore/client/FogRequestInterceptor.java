package at.sintrum.fog.clientcore.client;

import at.sintrum.fog.core.service.EnvironmentInfoService;
import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * Created by Michael Mittermayr on 25.07.2017.
 */

public class FogRequestInterceptor implements RequestInterceptor {

    private final EnvironmentInfoService environmentInfoService;

    public FogRequestInterceptor(EnvironmentInfoService environmentInfoService) {
        this.environmentInfoService = environmentInfoService;
    }

    @Override
    public void apply(RequestTemplate template) {
        template.header("FogId", environmentInfoService.getFogId());
    }
}
