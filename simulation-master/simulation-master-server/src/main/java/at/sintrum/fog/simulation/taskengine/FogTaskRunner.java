package at.sintrum.fog.simulation.taskengine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by Michael Mittermayr on 24.08.2017.
 */
@Service
public class FogTaskRunner {

    private FogTaskList taskList;

    private static final Logger LOG = LoggerFactory.getLogger(FogTaskRunner.class);

    @Scheduled(fixedDelay = 1000)
    public void executor() {

        if (taskList == null || !taskList.isReady()) {
            return;
        }

        List<Future<Boolean>> waitList = new ArrayList<>();

        for (Integer id : taskList.getIds()) {
            Future<Boolean> execute = taskList.execute(id);
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

    public void setTaskList(FogTaskList taskList) {
        this.taskList = taskList;
    }
}
