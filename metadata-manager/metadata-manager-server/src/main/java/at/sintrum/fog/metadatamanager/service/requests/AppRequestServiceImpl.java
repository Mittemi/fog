package at.sintrum.fog.metadatamanager.service.requests;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.metadatamanager.api.dto.AppRequest;
import at.sintrum.fog.metadatamanager.api.dto.AppRequestResult;
import at.sintrum.fog.metadatamanager.api.dto.DockerContainerMetadata;
import at.sintrum.fog.metadatamanager.api.dto.DockerImageMetadata;
import at.sintrum.fog.metadatamanager.service.ContainerMetadataService;
import at.sintrum.fog.metadatamanager.service.ImageMetadataService;
import com.google.common.collect.ImmutableList;
import org.redisson.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Michael Mittermayr on 28.10.2017.
 */
@Service
public class AppRequestServiceImpl {

    private final RedissonClient redissonClient;
    private final ContainerMetadataService containerMetadataService;
    private final ImageMetadataService imageMetadataService;


    private final Logger LOG = LoggerFactory.getLogger(AppRequestServiceImpl.class);

    public AppRequestServiceImpl(RedissonClient redissonClient, ContainerMetadataService containerMetadataService, ImageMetadataService imageMetadataService) {
        this.redissonClient = redissonClient;
        this.containerMetadataService = containerMetadataService;
        this.imageMetadataService = imageMetadataService;
    }

    public AppRequestResult request(AppRequest appRequest) {
        AppRequestInfo requestInfo = new AppRequestInfo(appRequest);
        boolean result = getTravelQueueByInstanceId(appRequest.getInstanceId()).add(requestInfo);
        return result ? new AppRequestResult(requestInfo.getInternalId()) : null;
    }

    private RSet<String> getMetadataList() {
        return redissonClient.getSet("App_Travel_Known_Apps");
    }

    private RList<AppRequestInfo> getTravelQueueByInstanceId(String instanceId) {

        DockerContainerMetadata containerMetadata = containerMetadataService.getLatestByInstance(instanceId);
        DockerImageMetadata imageMetadata = imageMetadataService.get(null, containerMetadata.getImageMetadataId());
        String name = "App_Travel_" + imageMetadata.getApplicationName();
        return getTravelQueue(name);
    }

    private RList<AppRequestInfo> getTravelQueue(String name) {
        boolean add = getMetadataList().add(name);

        RList<AppRequestInfo> result = redissonClient.getList(name);
        if (add) {
            //    result.trySetComparator(new SortByDateComparator());
        }
        return result;
    }

    public void reset() {
        for (String name : getMetadataList().readAll()) {
            getTravelQueue(name).delete();
        }
        getMetadataList().delete();
        getFinishedRequestsMap().delete();
    }

    public List<String> getKnownApps() {
        return ImmutableList.copyOf(getMetadataList().readAll());
    }

    public RBucket<AppRequestInfo> getActiveRequestBucket(String instanceId) {
        return redissonClient.getBucket("App_Travel_Active_Request" + instanceId);
    }

    public RMap<String, AppRequestInfo> getFinishedRequestsMap() {
        return redissonClient.getMap("App_Travel_Finished_Map");
    }

    public AppRequest getNextRequest(String instanceId) {
        AppRequestInfo peek = getTravelQueueByInstanceId(instanceId).stream().min(Comparator.comparing(AppRequestInfo::getCreationDate)).orElse(null);
        if (peek == null) return null;
        getActiveRequestBucket(instanceId).set(peek);
        return peek.getAppRequest();
    }

    public AppRequest finishMove(String instanceId, FogIdentification currentFog) {

        RBucket<AppRequestInfo> activeRequestBucket = getActiveRequestBucket(instanceId);

        final AppRequestInfo active = activeRequestBucket.isExists() ? activeRequestBucket.get() : null;

        if (active == null) {
            LOG.warn("No active move. Finish not possible!");
        }

        RList<AppRequestInfo> travelQueue = getTravelQueueByInstanceId(instanceId);

        List<AppRequestInfo> removeList = travelQueue.readAll().stream().filter(appRequestInfo -> (active == null || appRequestInfo.getInternalId().equals(active.getInternalId())) && appRequestInfo.getTargetFog().equals(currentFog.toFogId())).collect(Collectors.toList());
        if (removeList.size() > 0) {
            travelQueue.removeAll(removeList);
        } else {
            return null;
        }

        if (removeList.size() != 1) {
            LOG.warn("FinishedMove: affected apps list contains != 1 elements. (" + removeList.size() + " Elements)");
        }

        for (AppRequestInfo appRequestInfo : removeList) {
            getFinishedRequestsMap().put(appRequestInfo.getInternalId(), appRequestInfo);
        }
        activeRequestBucket.delete();
        return active == null ? null : active.getAppRequest();      //dangerous
    }
}
