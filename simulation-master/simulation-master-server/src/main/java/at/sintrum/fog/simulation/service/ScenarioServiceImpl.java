package at.sintrum.fog.simulation.service;

import at.sintrum.fog.simulation.SimulationMasterInfoContributor;
import at.sintrum.fog.simulation.scenario.Scenario;
import at.sintrum.fog.simulation.scenario.dto.BasicScenarioInfo;
import at.sintrum.fog.simulation.scenario.dto.ScenarioExecutionInfo;
import at.sintrum.fog.simulation.scenario.dto.TrackExecutionInfo;
import at.sintrum.fog.simulation.simulation.ScenarioExecutionResult;
import at.sintrum.fog.simulation.taskengine.TaskListAsyncInvoker;
import at.sintrum.fog.simulation.taskengine.TaskListBuilder;
import at.sintrum.fog.simulation.taskengine.TrackExecutionState;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by Michael Mittermayr on 02.09.2017.
 */
@Service
public class ScenarioServiceImpl implements ScenarioService {

    private final ConcurrentHashMap<String, Scenario> scenarios;
    private static final Logger LOG = LoggerFactory.getLogger(ScenarioServiceImpl.class);
    private final TaskListAsyncInvoker taskListAsyncInvoker;
    private final SimulationMasterInfoContributor simulationMasterInfoContributor;
    private TaskListBuilder.TaskListBuilderState taskList;

    private ScenarioExecutionResult executionResult;

    public ScenarioExecutionResult getExecutionResult() {
        return executionResult;
    }

    public ScenarioServiceImpl(Set<Scenario> scenarioList, TaskListAsyncInvoker taskListAsyncInvoker, SimulationMasterInfoContributor simulationMasterInfoContributor) {
        this.taskListAsyncInvoker = taskListAsyncInvoker;
        this.simulationMasterInfoContributor = simulationMasterInfoContributor;
        scenarios = new ConcurrentHashMap<>();
        for (Scenario scenario : scenarioList) {
            scenarios.put(scenario.getId(), scenario);
        }
    }

    @Override
    public List<String> getScenarioNames() {
        return Collections.list(scenarios.keys());
    }

    @Override
    public ScenarioExecutionInfo run(BasicScenarioInfo basicScenarioInfo, String name, boolean useAuction) {

        //TODO: improve
        if (taskList != null && !taskList.isFinished()) {
            return null;
        }
        Scenario scenario = scenarios.get(name);
        taskList = scenario.build(basicScenarioInfo, useAuction);
        executionResult = new ScenarioExecutionResult(scenario.getId(), basicScenarioInfo, new DateTime(), useAuction);
        return getExecutionState();
    }

    @Override
    public ScenarioExecutionInfo getExecutionState() {
        if (taskList == null) return null;

        List<TrackExecutionInfo> trackExecutionInfos = new LinkedList<>();
        ScenarioExecutionInfo scenarioExecutionInfo = new ScenarioExecutionInfo(taskList.getScenario().getId(), taskList.isFinished(), trackExecutionInfos, executionResult.isUseAuctioning());

        for (Integer trackId : taskList.getTrackIds()) {
            TrackExecutionState trackState = taskList.getTrackState(trackId);
            trackExecutionInfos.add(new TrackExecutionInfo(trackId, taskList.isFinished(trackId), trackState.getLogging().getLogs(), trackState.getCurrentTaskIndex()));
        }

        return scenarioExecutionInfo;
    }

    @Override
    @Async
    public boolean cancel() {
        if (this.taskList == null) {
            return true;
        }
        TaskListBuilder.TaskListBuilderState lTaskList = this.taskList;
        this.taskList = null;
        executionResult = null;
        this.simulationMasterInfoContributor.setFogRequestsManager(null);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
        lTaskList.cancel();
        return true;
    }

    @Scheduled(fixedDelay = 1000)
    public void executor() {

        TaskListBuilder.TaskListBuilderState taskList = this.taskList;

        if (taskList == null) {
            return;
        }

        List<Future<Boolean>> waitList = new ArrayList<>();

        for (Integer id : taskList.getTrackIds()) {
            Future<Boolean> execute = taskListAsyncInvoker.execute(taskList, id);
            waitList.add(execute);
        }

        for (Future<Boolean> booleanFuture : waitList) {
            try {
                booleanFuture.get();
            } catch (InterruptedException e) {
                LOG.error("FogTaskRunner: ", e);
            } catch (ExecutionException e) {
                LOG.error("FogTaskRunner: ", e);
            }
        }
    }

}
