package at.sintrum.fog.simulation.service;

import at.sintrum.fog.applicationhousing.client.api.AppEvolutionClient;
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
import org.springframework.util.StringUtils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Michael Mittermayr on 08.08.2017.
 */
@Service
public class SimulationServiceImpl implements SimulationService {

    private static final Logger LOG = LoggerFactory.getLogger(SimulationServiceImpl.class);
    private final ScenarioService scenarioService;
    private final AppEvolutionClient appEvolutionClient;
    private final ImageMetadataClient imageMetadataClient;
    private final SimulationDbEntryRepository simulationDbEntryRepository;

    private final ConcurrentHashMap<String, String> imageMetadataMap;


    public SimulationServiceImpl(ScenarioService scenarioService,
                                 AppEvolutionClient appEvolutionClient,
                                 ImageMetadataClient imageMetadataClient,
                                 SimulationDbEntryRepository simulationDbEntryRepository) {

        this.scenarioService = scenarioService;
        this.appEvolutionClient = appEvolutionClient;
        this.imageMetadataClient = imageMetadataClient;
        this.simulationDbEntryRepository = simulationDbEntryRepository;
        imageMetadataMap = new ConcurrentHashMap<String, String>();
    }


    @Override
    public void processOperation(String instanceId, AppEvent appEvent, AppEventInfo eventInfo) {
        ScenarioExecutionResult executionResult = scenarioService.getExecutionResult();

        if (executionResult == null) {
            LOG.error("No running scenario! Can't log event!");
            return;
        }

        SimulationDbEntry simulationDbEntry = new SimulationDbEntry();
        simulationDbEntry.setAppEventInfo(eventInfo);
        simulationDbEntry.setSimulationRunId(executionResult.getExecutionId());

        String imageMetadataId = getImageMetadataId(eventInfo);
        AppExecutionLogging appExecutionLogging = executionResult.addOrGetAppExecutionLogging(imageMetadataId);

        appExecutionLogging.addEvent(appEvent);
    }

    private String getImageMetadataId(AppEventInfo eventInfo) {

        String result = imageMetadataMap.getOrDefault(eventInfo.getImageMetadataId(), null);

        if (StringUtils.isEmpty(result)) {
            DockerImageMetadata baseImageMetadata = imageMetadataClient.getBaseImageMetadata(eventInfo.getImageMetadataId());
            if (baseImageMetadata != null) {
                imageMetadataMap.put(eventInfo.getImageMetadataId(), baseImageMetadata.getId());
                return baseImageMetadata.getId();
            } else {
                return eventInfo.getImageMetadataId();
            }
        }

        return result;
    }
}
