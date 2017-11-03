package at.sintrum.fog.simulation.service;

import at.sintrum.fog.metadatamanager.api.dto.DockerImageMetadata;
import at.sintrum.fog.metadatamanager.client.api.ImageMetadataClient;
import at.sintrum.fog.simulation.api.dto.AppEventInfo;
import at.sintrum.fog.simulation.simulation.AppEvent;
import at.sintrum.fog.simulation.simulation.AppExecutionLogging;
import at.sintrum.fog.simulation.simulation.ScenarioExecutionResult;
import at.sintrum.fog.simulation.simulation.mongo.SimulationDbEntry;
import at.sintrum.fog.simulation.simulation.mongo.respositories.SimulationDbEntryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Michael Mittermayr on 08.08.2017.
 */
@Service
public class SimulationServiceImpl implements SimulationService {

    private static final Logger LOG = LoggerFactory.getLogger(SimulationServiceImpl.class);
    private final ScenarioService scenarioService;
    private final ImageMetadataClient imageMetadataClient;
    private final SimulationDbEntryRepository simulationDbEntryRepository;

    private final ConcurrentHashMap<String, DockerImageMetadata> imageMetadataMap;


    public SimulationServiceImpl(ScenarioService scenarioService,
                                 ImageMetadataClient imageMetadataClient,
                                 SimulationDbEntryRepository simulationDbEntryRepository) {

        this.scenarioService = scenarioService;
        this.imageMetadataClient = imageMetadataClient;
        this.simulationDbEntryRepository = simulationDbEntryRepository;
        imageMetadataMap = new ConcurrentHashMap<>();
    }


    @Override
    public void processOperation(String instanceId, AppEvent appEvent, AppEventInfo eventInfo) {
        ScenarioExecutionResult executionResult = scenarioService.getExecutionResult();

        if (executionResult == null) {
            LOG.error("No running scenario! Can't log event!");
            return;
        }

        String imageMetadataId = getImageMetadataId(eventInfo);

        AppExecutionLogging appExecutionLogging = executionResult.addOrGetAppExecutionLogging(imageMetadataId);
        appExecutionLogging.addEvent(appEvent);

        SimulationDbEntry simulationDbEntry = new SimulationDbEntry();
        simulationDbEntry.setAppEventInfo(eventInfo);
        simulationDbEntry.setAppEvent(appEvent);
        simulationDbEntry.setMetadataId(imageMetadataId);
        simulationDbEntry.setAppName(getAppName(eventInfo));
        simulationDbEntry.setSimulationRunId(executionResult.getExecutionId());

        simulationDbEntryRepository.save(simulationDbEntry);
    }

    private String getAppName(AppEventInfo eventInfo) {
        DockerImageMetadata result = getImageMetadata(eventInfo);
        if (result != null) {
            return result.getApplicationName();
        } else {
            return "unknown";
        }
    }

    private String getImageMetadataId(AppEventInfo eventInfo) {

        DockerImageMetadata result = getImageMetadata(eventInfo);
        if (result != null) {
            return result.getId();
        } else {
            return eventInfo.getImageMetadataId();
        }
    }

    private DockerImageMetadata getImageMetadata(AppEventInfo eventInfo) {
        DockerImageMetadata result = imageMetadataMap.getOrDefault(eventInfo.getImageMetadataId(), null);

        if (result == null) {
            result = imageMetadataClient.getBaseImageMetadata(eventInfo.getImageMetadataId());
            if (result != null) {
                imageMetadataMap.put(eventInfo.getImageMetadataId(), result);
            }
        }

        return result;
    }
}