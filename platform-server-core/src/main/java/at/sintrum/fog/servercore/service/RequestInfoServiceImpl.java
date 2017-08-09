package at.sintrum.fog.servercore.service;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Michael Mittermayr on 25.07.2017.
 */
@Component
@Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestInfoServiceImpl implements RequestInfoService {

    private HttpServletRequest servletRequest;

    public RequestInfoServiceImpl(HttpServletRequest servletRequest) {
        this.servletRequest = servletRequest;
    }

    public String getCallerFogId() {
        return servletRequest.getHeader("CallerFogId");
    }

    public String getCallerServiceUrl() {
        return "http://" + getCallerFogId();
    }
}
