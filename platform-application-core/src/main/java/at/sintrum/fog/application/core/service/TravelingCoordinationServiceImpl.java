package at.sintrum.fog.application.core.service;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.core.service.EnvironmentInfoService;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

/**
 * Created by Michael Mittermayr on 17.07.2017.
 */
@Service
public class TravelingCoordinationServiceImpl implements TravelingCoordinationService, ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(TravelingCoordinationServiceImpl.class);

    private final RedissonClient redissonClient;
    private final EnvironmentInfoService environmentInfoService;

    public TravelingCoordinationServiceImpl(RedissonClient redissonClient, EnvironmentInfoService environmentInfoService) {
        this.redissonClient = redissonClient;
        this.environmentInfoService = environmentInfoService;
    }

    @Override
    public boolean requestMove(FogIdentification fogIdentification) {

        try {
            getTravelQueue().add(fogIdentification);
            return true;
        } catch (Exception ex) {
            LOG.error("Failed to add to request queue", ex);
        }
        return false;
    }

    private RQueue<FogIdentification> getTravelQueue() {
        return redissonClient.getQueue("App_Travel_" + environmentInfoService.getApplicationName());
    }

    @Override
    public boolean hasNextTarget() {
        try {
            return !getTravelQueue().isEmpty();
        } catch (Exception ex) {
            LOG.error("Failed to check if there is another travel target", ex);
            return true;    //lets assume there is one, due to concurrency the code has to prevent an error in case there is none anyways
        }
    }

    @Override
    public FogIdentification getNextTarget() {

        try {
            return getTravelQueue().poll();
        } catch (Exception ex) {
            LOG.error("Failed to get next travel target", ex);
            return null;
        }
    }

    @Override
    public boolean finishMove(FogIdentification fogIdentification) {

        try {
            FogIdentification peek = getTravelQueue().peek();
            if (peek != null && peek.isSameFog(fogIdentification)) {
                if (!getTravelQueue().remove(peek)) {
                    LOG.error("Element not removed from queue, even though it should have been in there");
                    return false;
                }
            }
            return true;
        } catch (Exception ex) {
            LOG.error("Failed to update travel requests queue", ex);
            return false;
        }
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        LOG.debug("App start finished. Let's finish the move operation");
        FogIdentification fogIdentification = FogIdentification.parseFogBaseUrl(environmentInfoService.getFogBaseUrl());
        finishMove(fogIdentification);
    }
}
