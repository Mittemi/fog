package at.sintrum.fog.clientcore.service;

import at.sintrum.fog.clientcore.client.ClientFactoryFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Michael Mittermayr on 01.06.2017.
 */
@Service
public class ShutdownApplicationServiceImpl implements ShutdownApplicationService {

    private ClientFactoryFactory clientFactoryFactory;

    public ShutdownApplicationServiceImpl(ClientFactoryFactory clientFactoryFactory) {

        this.clientFactoryFactory = clientFactoryFactory;
    }

    @Override
    public void shutdown(String url) {
        clientFactoryFactory.buildClient(ShutdownEndpoint.class, url).shutdown();
    }

    interface ShutdownEndpoint {

        @RequestMapping(value = "shutdown", method = RequestMethod.POST)
        void shutdown();
    }
}
