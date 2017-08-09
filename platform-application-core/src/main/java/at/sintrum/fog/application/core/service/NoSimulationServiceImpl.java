package at.sintrum.fog.application.core.service;

import at.sintrum.fog.core.dto.FogIdentification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * Created by Michael Mittermayr on 09.08.2017.
 */
@ConditionalOnProperty(prefix = "fog.app", name = "enable-simulation-mode", havingValue = "false", matchIfMissing = true)
@Service
public class NoSimulationServiceImpl implements SimulationClientService {

    private static final Logger LOG = LoggerFactory.getLogger(NoSimulationServiceImpl.class);

    public NoSimulationServiceImpl() {
        LOG.debug("Simulation mode disabled");
    }

    @Override
    public void sendHeartbeat() {

    }

    @Override
    public void notifyStarting() {

    }

    @Override
    public void notifyMove(FogIdentification target) {

    }

    @Override
    public void notifyMoved() {

    }

    @Override
    public void notifyStandby() {

    }

    @Override
    public void notifyRunning() {

    }

    @Override
    public void notifyUpgrade() {

    }
}
