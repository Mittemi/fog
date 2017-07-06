package at.sintrum.fog.deploymentmanager.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

/**
 * Created by Michael Mittermayr on 06.07.2017.
 */
public class FogAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(FogAsyncUncaughtExceptionHandler.class);

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        if (logger.isErrorEnabled()) {
            logger.error(String.format("Unexpected error occurred invoking async " +
                    "method '%s'.", method), ex);
        }
    }
}
