package at.sintrum.fog.simulation.taskengine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

/**
 * Created by Michael Mittermayr on 24.08.2017.
 */
@Service
public class TaskListAsyncInvoker {

    private static final Logger LOG = LoggerFactory.getLogger(TaskListAsyncInvoker.class);

    @Async
    public Future<Boolean> execute(TaskListBuilder.TaskListBuilderState taskList, int id) {
        return new AsyncResult<>(taskList.runTaskIfPossible(id));
    }
}
