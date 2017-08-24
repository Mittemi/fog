package at.sintrum.fog.simulation.taskengine.tasks;

/**
 * Created by Michael Mittermayr on 24.08.2017.
 */
public class NotifySimulationTrackFinishedTask extends FogTaskBase {

    private final int id;

    public NotifySimulationTrackFinishedTask(int offset, int id) {
        super(offset, NotifySimulationTrackFinishedTask.class);
        this.id = id;
    }

    @Override
    protected boolean internalExecute() {
        getLogger().info("Track finished: " + id);
        return true;
    }
}
