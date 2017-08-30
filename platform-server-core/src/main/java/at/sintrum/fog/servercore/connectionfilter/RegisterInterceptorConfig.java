package at.sintrum.fog.servercore.connectionfilter;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Michael Mittermayr on 30.08.2017.
 */
@Configuration
public class RegisterInterceptorConfig extends WebMvcConfigurerAdapter {

    private final ServiceConnectionInterceptor serviceConnectionInterceptor;

    public RegisterInterceptorConfig(ServiceConnectionInterceptor serviceConnectionInterceptor) {
        this.serviceConnectionInterceptor = serviceConnectionInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(serviceConnectionInterceptor);
        super.addInterceptors(registry);
    }
}
