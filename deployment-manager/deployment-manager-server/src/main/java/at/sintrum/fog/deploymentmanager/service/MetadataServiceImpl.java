package at.sintrum.fog.deploymentmanager.service;

import at.sintrum.fog.core.service.EnvironmentInfoService;
import at.sintrum.fog.deploymentmanager.api.dto.ApplicationMoveRequest;
import at.sintrum.fog.deploymentmanager.api.dto.ApplicationStartRequest;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Mittermayr on 06.07.2017.
 */
@Service
public class MetadataServiceImpl {

    private final EnvironmentInfoService environmentInfoService;
    private final RedissonClient client;
    private final Logger LOG = LoggerFactory.getLogger(MetadataServiceImpl.class);

    public MetadataServiceImpl(EnvironmentInfoService environmentInfoService, RedissonClient client) {
        this.environmentInfoService = environmentInfoService;
        this.client = client;
    }

    public boolean addAppStartRequest(ApplicationStartRequest applicationStartRequest) {
        RAtomicLong appStartRequestState = getStartRequestState(applicationStartRequest);

//        if (!appStartRequestState.isExists()) {

        RQueue<String> fogStartQueue = getStartQueue();

        if (fogStartQueue.contains(applicationStartRequest.getMetadataId())) {
            LOG.warn("Startup request already in queue for Id: " + applicationStartRequest.getMetadataId());
            return false;
        } else {
            fogStartQueue.add(applicationStartRequest.getMetadataId());
            appStartRequestState.set(AppState.AllTodo.value);
            return true;
        }
//        } else {
//            LOG.warn("AppState exists, skip start");
//            return false;
//        }
    }

    public List<ApplicationStartRequest> getUnfinishedStartupRequests() {
        List<ApplicationStartRequest> startRequestList = new LinkedList<>();
        for (String id : getStartQueue().readAll()) {
//            ApplicationStartRequest applicationStartRequest = new ApplicationStartRequest(id);
//            RAtomicLong startRequestState = getStartRequestState(applicationStartRequest);
//            if (startRequestState.isExists()) {
//                if (startRequestState.get() != AppState.Done.value) {
//                    startRequestList.add(applicationStartRequest);
//                }
//            }
        }
        return startRequestList;
    }

    public List<ApplicationMoveRequest> getUnfinishedMoveRequests() {
        List<ApplicationMoveRequest> startRequestList = new LinkedList<>();
        for (ApplicationMoveRequest id : getMoveQueue().readAll()) {
//            RAtomicLong startRequestState = getStartRequestState(id);
//            if (startRequestState.isExists()) {
//                if (startRequestState.get() != AppState.Done.value) {
//                    startRequestList.add();
//                }
//            }
        }
        return startRequestList;
    }


    private RQueue<String> getStartQueue() {
        return client.getQueue("start_" + environmentInfoService.getFogId());
    }

    private RQueue<ApplicationMoveRequest> getMoveQueue() {
        return client.getQueue("move_" + environmentInfoService.getFogId());
    }

    private RAtomicLong getStartRequestState(ApplicationStartRequest applicationStartRequest) {
        return client.getAtomicLong(getInternalStartupRequestId(applicationStartRequest));
    }

    private String getInternalStartupRequestId(ApplicationStartRequest applicationStartRequest) {
        return "start_" + environmentInfoService.getFogId() + "_" + applicationStartRequest.getMetadataId();
    }

    private String getInternalMoveRequestId(ApplicationMoveRequest applicationMoveRequest) {
        return "move_" + environmentInfoService.getFogId() + "_" + applicationMoveRequest.getContainerId();
    }

    public void finishStartup(ApplicationStartRequest applicationStartRequest) {
        getStartRequestState(applicationStartRequest).set(AppState.Done.value);
        getStartQueue().remove(applicationStartRequest.getMetadataId());
    }

    public void startupFailed(ApplicationStartRequest applicationStartRequest) {
        getStartRequestState(applicationStartRequest).set(AppState.Failed.value);
    }

    enum AppState {

        Done(0),
        AllTodo(99),
        Failed(-1);

        private long value = 0;

        AppState(long value) {
            this.value = value;
        }

    }
}
