package at.sintrum.fog.application.core;

import at.sintrum.fog.application.core.service.ApplicationLifecycleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Michael Mittermayr on 28.08.2017.
 */
//@Service
public class FogAppRequestFilter implements Filter {

    private final ApplicationLifecycleService applicationLifecycleService;
    private static final Logger LOG = LoggerFactory.getLogger(FogAppRequestFilter.class);

    public FogAppRequestFilter(ApplicationLifecycleService applicationLifecycleService) {
        this.applicationLifecycleService = applicationLifecycleService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!applicationLifecycleService.shouldAcceptRequests()) {
            LOG.warn("Cancel request. App is in maintenance mode.");
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setContentType("application/json");
            httpResponse.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "The applications is currently not accepting any new requests. Try later.");
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
