package at.sintrum.fog.application.core.service;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.core.service.EnvironmentInfoService;
import at.sintrum.fog.metadatamanager.api.ApplicationStateMetadataApi;
import at.sintrum.fog.metadatamanager.api.dto.ApplicationStateMetadata;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Michael Mittermayr on 17.07.2017.
 */
@Service
public class TravelingCoordinationServiceImpl implements TravelingCoordinationService {

    private static final Logger LOG = LoggerFactory.getLogger(TravelingCoordinationServiceImpl.class);

    private final RedissonClient redissonClient;
    private final EnvironmentInfoService environmentInfoService;
    private final ApplicationStateMetadataApi applicationStateMetadataClient;

    public TravelingCoordinationServiceImpl(RedissonClient redissonClient, EnvironmentInfoService environmentInfoService, ApplicationStateMetadataApi applicationStateMetadataClient) {
        this.redissonClient = redissonClient;
        this.environmentInfoService = environmentInfoService;
        this.applicationStateMetadataClient = applicationStateMetadataClient;
    }

    @Override
    public boolean requestMove(FogIdentification fogIdentification) {

        try {
            RQueue<FogIdentification> travelQueue = getTravelQueue();

            if (travelQueue.readAll().stream().anyMatch(x -> x.isSameFog(fogIdentification))) {
                LOG.warn("Travel-Queue already contains this a move request from fog: " + fogIdentification.toFogId());
                return false;
            }

            travelQueue.add(fogIdentification);
            return true;
        } catch (Exception ex) {
            LOG.error("Failed to add to request queue", ex);
        }
        return false;
    }

    @Override
    public List<FogIdentification> getTargets() {
        return getTravelQueue().readAll();
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
            return getTravelQueue().peek();
        } catch (Exception ex) {
            LOG.error("Failed to get next travel target", ex);
            return null;
        }
    }

    @Override
    public boolean startMove(FogIdentification target) {

        return true;
    }

    @Override
    public boolean finishMove(FogIdentification currentFog) {

        ApplicationStateMetadata state = applicationStateMetadataClient.getById(environmentInfoService.getInstanceId());
        FogIdentification target = state.getNextTarget();

        if (currentFog.isSameFog(state.getRunningAt())) {
            LOG.debug("Still running at the same fog");
        }

        if (target != null && !target.isSameFog(currentFog)) {
            LOG.warn("App target fog != startup fog");
            return true;
        }

        try {
            return removeTargetFromQueue(currentFog);
        } catch (Exception ex) {
            LOG.error("Failed to update travel requests queue", ex);
            return false;
        }
    }

    private boolean removeTargetFromQueue(FogIdentification currentFog) {
        FogIdentification peek = getTravelQueue().peek();
        if (peek != null) {
            if (peek.isSameFog(currentFog)) {
                if (!getTravelQueue().remove(peek)) {
                    LOG.error("Element not removed from queue, even though it should have been in there");
                    return false;
                }
            } else {
                LOG.info("Nothing removed from target queue. Maybe this was an app upgrade.");
                return true;
            }
        }
        return true;
    }
}
