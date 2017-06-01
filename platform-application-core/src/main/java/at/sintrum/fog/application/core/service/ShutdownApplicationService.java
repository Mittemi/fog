package at.sintrum.fog.application.core.service;

import at.sintrum.fog.clientcore.client.ClientFactoryFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Michael Mittermayr on 01.06.2017.
 */
public class ShutdownApplicationService {

    private ClientFactoryFactory clientFactoryFactory;

    public ShutdownApplicationService(ClientFactoryFactory clientFactoryFactory) {

        this.clientFactoryFactory = clientFactoryFactory;
    }

    public void shutdown(String url) {
        clientFactoryFactory.buildClient(ShutdownEndpoint.class, url).shutdown();
    }

    interface ShutdownEndpoint {

        @RequestMapping(value = "shutdown", method = RequestMethod.POST)
        void shutdown();
    }
}
