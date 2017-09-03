package at.sintrum.fog.simulation.service;

import at.sintrum.fog.simulation.scenario.Scenario;
import at.sintrum.fog.simulation.scenario.dto.BasicScenarioInfo;
import at.sintrum.fog.simulation.scenario.dto.ScenarioExecutionInfo;
import at.sintrum.fog.simulation.taskengine.TaskListAsyncInvoker;
import at.sintrum.fog.simulation.taskengine.TaskListBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
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
    private TaskListBuilder.TaskListBuilderState taskList;

    public ScenarioServiceImpl(Set<Scenario> scenarioList, TaskListAsyncInvoker taskListAsyncInvoker) {
        this.taskListAsyncInvoker = taskListAsyncInvoker;
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
    public ScenarioExecutionInfo run(BasicScenarioInfo basicScenarioInfo, String name) {

        //TODO: improve
        if (taskList != null) {
            return null;
        }
        Scenario scenario = scenarios.get(name);
        taskList = scenario.build(basicScenarioInfo);
        return new ScenarioExecutionInfo(name);
    }


    @Scheduled(fixedDelay = 1000)
    public void executor() {

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
