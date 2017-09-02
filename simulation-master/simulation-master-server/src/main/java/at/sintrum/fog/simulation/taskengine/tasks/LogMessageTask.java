package at.sintrum.fog.simulation.taskengine.tasks;

/**
 * Created by Michael Mittermayr on 24.08.2017.
 */
public class LogMessageTask extends FogTaskBase {

    private final String message;
    private final String appInstanceId;

    public LogMessageTask(int offset, String message, String appInstanceId) {
        super(offset, LogMessageTask.class);
        this.message = message;
        this.appInstanceId = appInstanceId;
    }

    @Override
    protected boolean internalExecute() {
        getLogger().info("APP: " + appInstanceId + " || " + message);
        return true;
    }
}
