package at.sintrum.fog.metadatamanager.service.requests;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.metadatamanager.api.dto.*;
import at.sintrum.fog.metadatamanager.config.MetadataManagerConfigProperties;
import at.sintrum.fog.metadatamanager.service.ContainerMetadataService;
import at.sintrum.fog.metadatamanager.service.ImageMetadataService;
import com.google.common.collect.ImmutableList;
import org.joda.time.DateTime;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Michael Mittermayr on 28.10.2017.
 */
@Service
public class AppRequestServiceImpl {

    private final RedissonClient redissonClient;
    private final ContainerMetadataService containerMetadataService;
    private final ImageMetadataService imageMetadataService;
    private final MetadataManagerConfigProperties configProperties;


    private final Logger LOG = LoggerFactory.getLogger(AppRequestServiceImpl.class);

    public AppRequestServiceImpl(RedissonClient redissonClient, ContainerMetadataService containerMetadataService, ImageMetadataService imageMetadataService, MetadataManagerConfigProperties configProperties) {
        this.redissonClient = redissonClient;
        this.containerMetadataService = containerMetadataService;
        this.imageMetadataService = imageMetadataService;
        this.configProperties = configProperties;

        if (configProperties.isUseAuction()) {
            LOG.debug("Auctioning is enabled!");
        }
    }

    public AppRequestResult request(int credits, String internalId, AppRequest appRequest) {
        if (StringUtils.isEmpty(internalId)) {
            LOG.debug("Add new request");
            AppRequestInfo requestInfo = null;
            do {
                requestInfo = new AppRequestInfo(appRequest, credits);
            } while (getInternalIdQueueNameLookup().containsKey(requestInfo.getInternalId()));
            String queueName = getQueueName(appRequest.getInstanceId());
            getTravelQueue(queueName).put(requestInfo.getInternalId(), requestInfo);
            getInternalIdQueueNameLookup().put(requestInfo.getInternalId(), queueName);

            return new AppRequestResult(requestInfo.getInternalId(), credits);
        } else if (configProperties.isUseAuction()) {
            LOG.debug("Bid for existing request");
            AppRequestInfo appRequestInfo = getTravelQueueByInstanceId(appRequest.getInstanceId()).get(internalId);
            appRequestInfo.incrementCredits(credits);
            return new AppRequestResult(internalId, appRequestInfo.getCredits());
        } else {
            LOG.debug("Auction disabled!");
            return new AppRequestResult(internalId, credits);
        }
    }

    private RSet<String> getMetadataList() {
        return redissonClient.getSet("App_Travel_Known_Apps");
    }

    private RMap<String, String> getInternalIdQueueNameLookup() {
        return redissonClient.getMap("App_Travel_QueueName_Lookup");
    }

    private RMap<String, AppRequestInfo> getTravelQueueByInstanceId(String instanceId) {

        String name = getQueueName(instanceId);
        return getTravelQueue(name);
    }

    private String getQueueName(String instanceId) {
        DockerContainerMetadata containerMetadata = containerMetadataService.getLatestByInstance(instanceId);
        DockerImageMetadata imageMetadata = imageMetadataService.get(null, containerMetadata.getImageMetadataId());
        return buildQueueName(imageMetadata.getApplicationName());
    }

    private String buildQueueName(String appName) {
        return "App_Travel_" + appName;
    }

    private RMap<String, AppRequestInfo> getTravelQueue(String name) {
        getMetadataList().add(name);
        return redissonClient.getMap(name);
    }

    public void reset() {
        for (String name : getMetadataList().readAll()) {
            LOG.debug("Delete queue for: " + name);
            getTravelQueue(name).delete();
        }
        getMetadataList().delete();
        getInternalIdQueueNameLookup().delete();
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
        AppRequestInfo peek = getTravelQueueByInstanceId(instanceId).values().stream().sorted(getNextRequestComparator()).findFirst().orElse(null);
        if (peek == null) return null;
        getActiveRequestBucket(instanceId).set(peek);
        return peek.getAppRequest();
    }

    private Comparator<AppRequestInfo> getNextRequestComparator() {
        if (configProperties.isUseAuction()) {
            return Comparator.comparing(AppRequestInfo::getCredits).reversed();
        }
        return Comparator.comparing(AppRequestInfo::getCreationDate);
    }

    public List<AppRequestInfo> getFinishedRequests() {
        return new ArrayList<>(getFinishedRequestsMap().values());
    }

    public AppRequest finishMove(String instanceId, FogIdentification currentFog) {

        RBucket<AppRequestInfo> activeRequestBucket = getActiveRequestBucket(instanceId);

        final AppRequestInfo active = activeRequestBucket.isExists() ? activeRequestBucket.get() : null;

        if (active == null) {
            //Should only be the case for the cloud (there it is a valid thing)
            LOG.warn("No active move. Finish not possible! Fog: " + currentFog.toFogId() + " InstanceID: " + instanceId);
            if (!currentFog.getIp().equals("192.168.1.21")) {
                LOG.error("This could be a problem! Check it. No active move set for target != cloud.");
            }
        }

        RMap<String, AppRequestInfo> travelQueue = getTravelQueueByInstanceId(instanceId);

        List<AppRequestInfo> removeList = getAffectedRequests(currentFog, active, travelQueue.values());
        if (removeList.size() > 0) {
            for (AppRequestInfo appRequestInfo : removeList) {
                appRequestInfo.setFinishedDate(new DateTime());
                travelQueue.remove(appRequestInfo.getInternalId());
                getFinishedRequestsMap().put(appRequestInfo.getInternalId(), appRequestInfo);
            }
        } else {
            return null;
        }

        if (removeList.size() != 1) {
            LOG.warn("FinishedMove: affected apps list contains != 1 elements. (" + removeList.size() + " Elements)");
        }
        activeRequestBucket.delete();
        return active == null ? null : active.getAppRequest();
    }

    protected List<AppRequestInfo> getAffectedRequests(FogIdentification currentFog, AppRequestInfo activeRequestInfo, Collection<AppRequestInfo> travelQueue) {
        Stream<AppRequestInfo> stream = travelQueue.stream();

        if (activeRequestInfo != null) {
            stream = stream.filter(appRequestInfo -> (appRequestInfo.getInternalId().equals(activeRequestInfo.getInternalId())));
        }

        stream = stream.filter(appRequestInfo -> appRequestInfo.getTargetFog().equals(currentFog.toFogId()));

        return stream.collect(Collectors.toList());
    }

    public RequestState requestInfo(String internalId) {
        String queueName = getInternalIdQueueNameLookup().get(internalId);

        if (StringUtils.isEmpty(queueName)) {
            return null;
        }

        AppRequestInfo requestInfo = getFinishedRequestsMap().get(internalId);
        if (requestInfo == null) {
            requestInfo = getTravelQueue(queueName).get(internalId);
        }

        if (requestInfo == null) {
            return null;
        }
        return new RequestState(requestInfo.getInternalId(), requestInfo.getCredits(), requestInfo.getCreationDate(), getFinishedRequestsMap().containsKey(internalId));
    }

    public void reset(String appName) {
        getTravelQueue(buildQueueName(appName)).delete();
    }

    public List<AppRequest> getRequests(String appName) {
        return getTravelQueue(buildQueueName(appName)).values().stream().filter(x -> x.getFinishedDate() == null).map(AppRequestInfo::getAppRequest).collect(Collectors.toList());
    }

    public List<AppRequestInfo> getNextRequests() {
        List<AppRequestInfo> result = new LinkedList<>();
        for (String app : getKnownApps()) {
            AppRequestInfo requestInfo = getTravelQueue(app).values().stream().sorted(getNextRequestComparator()).findFirst().orElse(null);

            if (requestInfo != null) {
                result.add(requestInfo);
            }
        }
        return result;
    }
}
