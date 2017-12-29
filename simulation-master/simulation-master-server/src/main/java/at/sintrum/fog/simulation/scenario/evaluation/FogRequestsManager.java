package at.sintrum.fog.simulation.scenario.evaluation;

import at.sintrum.fog.applicationhousing.client.api.AppEvolutionClient;
import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.metadatamanager.client.api.AppRequestClient;
import at.sintrum.fog.simulation.service.FogCellStateService;
import at.sintrum.fog.simulation.taskengine.AppRequestState;
import at.sintrum.fog.simulation.taskengine.TrackExecutionState;
import at.sintrum.fog.simulation.taskengine.tasks.FogTaskBase;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Transient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Michael Mittermayr on 31.10.2017.
 */
public class FogRequestsManager extends FogTaskBase {
    private final List<TrackExecutionState> applicationTracks;
    private final Map<String, Integer> fogCredits;
    private final int secondsBetweenRequests;
    private final AppRequestClient appRequestClient;
    private DateTime simulationStart;

    private DateTime lastIteration;

    private List<RequestInfo> requestInfos;
    private AppEvolutionClient appEvolutionClient;
    private final FogCellStateService fogCellStateService;

    public String getState() {
        long cntUnfinished = requestInfos.stream().filter(x -> !x.isFinished()).count();
        return "Unfinished requests: " + cntUnfinished + " out of: " + requestInfos.size();
    }

    private List<RequestInfo> getCurrentRequests() {
        final int currentOffset = Seconds.secondsBetween(simulationStart, new DateTime()).getSeconds();

        return requestInfos.stream().filter(x -> !x.isFinished())
                .filter(x -> x.offset <= currentOffset)
                .collect(Collectors.toList());
    }

    private Logger LOG = LoggerFactory.getLogger(FogRequestsManager.class);

    public FogRequestsManager(List<TrackExecutionState> applicationTracks,
                              Map<String, Integer> fogCredits,
                              int secondsBetweenRequests,
                              AppRequestClient appRequestClient,
                              List<RequestInfo> requestInfos,
                              AppEvolutionClient appEvolutionClient,
                              FogCellStateService fogCellStateService) {
        super(0, null, FogRequestsManager.class);

        this.applicationTracks = applicationTracks;
        this.fogCredits = fogCredits;
        this.secondsBetweenRequests = secondsBetweenRequests;
        this.appRequestClient = appRequestClient;
        this.requestInfos = requestInfos;
        this.appEvolutionClient = appEvolutionClient;
        this.fogCellStateService = fogCellStateService;
    }

    public void start() {
        simulationStart = new DateTime().minusMinutes(4);
    }

    public void nextIteration() {
        if (simulationStart == null) {
            LOG.debug("Simulation not yet started");
            return;
        }
        if (lastIteration != null && Seconds.secondsBetween(lastIteration, new DateTime()).isLessThan(Seconds.seconds(10))) {
            return;
        }

        lastIteration = new DateTime();
        List<RequestInfo> currentRequests = getCurrentRequests();
        for (RequestInfo requestInfo : currentRequests) {
            updateRequestState(appRequestClient, requestInfo.appRequestState);
        }

        List<RequestInfo> unfinishedRequests = currentRequests.stream().filter(x -> !x.isFinished()).collect(Collectors.toList());

        if (unfinishedRequests.size() > 0) {
            Map<String, List<RequestInfo>> appsPerFog = unfinishedRequests
                    .stream()
                    .collect(Collectors.groupingBy(x -> x.getFog().toFogId()));

            List<String> onlineFogs = appsPerFog.keySet().stream().filter(x -> fogCellStateService.isOnline(FogIdentification.parseFogId(x))).collect(Collectors.toList());

            // not yet finished requests
            for (RequestInfo requestInfo : unfinishedRequests) {
                String fogId = requestInfo.getFog().toFogId();
                if (!onlineFogs.contains(fogId)) {
                    LOG.debug("Fog " + fogId + " currently offline, skip bidding for this one!");
                    continue;
                }
                int credits = fogCredits.get(fogId) / appsPerFog.get(fogId).size();

                if (requestInfo.getLastRequest() == null || Seconds.secondsBetween(requestInfo.getLastRequest(), new DateTime()).isGreaterThan(Seconds.seconds(secondsBetweenRequests))) {
                    TrackExecutionState trackExecutionState = applicationTracks.get(requestInfo.applicationIndex);
                    updateInstanceId(appEvolutionClient, trackExecutionState);
                    requestApp(requestInfo.fog, requestInfo.estimatedDuration, credits, appRequestClient, requestInfo.appRequestState, trackExecutionState);
                    requestInfo.setLastRequest(new DateTime());
                }
            }
        }
    }

    public DateTime getSimulationStart() {
        return simulationStart;
    }

    @Override
    protected boolean internalExecute() {
        nextIteration();
        return requestInfos.stream().allMatch(RequestInfo::isFinished);
    }

    public static class RequestInfo {
        private int applicationIndex;
        private int offset;
        private int estimatedDuration;
        private FogIdentification fog;
        @Transient
        private AppRequestState appRequestState;

        @Transient
        private DateTime lastRequest;

        public RequestInfo() {
        }

        public RequestInfo(int applicationIndex, int offset, FogIdentification fog, int estimatedDuration) {
            this.applicationIndex = applicationIndex;
            this.offset = offset;
            this.fog = fog;
            this.estimatedDuration = estimatedDuration;
            appRequestState = new AppRequestState();
        }

        public int getApplicationIndex() {
            return applicationIndex;
        }

        public void setApplicationIndex(int applicationIndex) {
            this.applicationIndex = applicationIndex;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public AppRequestState getAppRequestState() {
            return appRequestState;
        }

        public boolean isFinished() {
            return appRequestState.isFinished();
        }

        public FogIdentification getFog() {
            return fog;
        }

        public void setFog(FogIdentification fog) {
            this.fog = fog;
        }

        public int getEstimatedDuration() {
            return estimatedDuration;
        }

        public void setEstimatedDuration(int estimatedDuration) {
            this.estimatedDuration = estimatedDuration;
        }

        public DateTime getLastRequest() {
            return lastRequest;
        }

        public void setLastRequest(DateTime lastRequest) {
            this.lastRequest = lastRequest;
        }
    }
}
