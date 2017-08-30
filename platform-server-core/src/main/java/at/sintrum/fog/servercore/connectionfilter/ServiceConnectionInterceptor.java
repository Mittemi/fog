package at.sintrum.fog.servercore.connectionfilter;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.servercore.service.RequestInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Michael Mittermayr on 30.08.2017.
 */
public class ServiceConnectionInterceptor extends HandlerInterceptorAdapter {

    private final ServiceStateInfoService serviceStateInfoService;
    private final RequestInfoService requestInfoService;
    private static final Logger LOG = LoggerFactory.getLogger(ServiceConnectionInterceptor.class);

    public ServiceConnectionInterceptor(ServiceStateInfoService serviceStateInfoService, RequestInfoService requestInfoService) {
        this.serviceStateInfoService = serviceStateInfoService;
        this.requestInfoService = requestInfoService;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if (AnnotationUtils.findAnnotation(handlerMethod.getMethod(), SimulationControlledOperation.class) != null)
                if (!serviceStateInfoService.isOnlineFor(FogIdentification.parseFogId(requestInfoService.getCallerFogId()))) {
                    LOG.debug("Service is down based on simulation input.");
                    HttpServletResponse httpResponse = (HttpServletResponse) response;
                    httpResponse.setContentType("application/json");
                    httpResponse.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "The service is currently down based on the simulation input.");
                    return false;
                }
        }
        return true;
    }
}
