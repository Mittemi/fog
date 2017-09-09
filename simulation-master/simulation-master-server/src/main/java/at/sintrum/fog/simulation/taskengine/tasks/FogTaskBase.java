package at.sintrum.fog.simulation.taskengine.tasks;

import at.sintrum.fog.simulation.taskengine.TrackExecutionState;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Michael Mittermayr on 24.08.2017.
 */
public abstract class FogTaskBase implements FogTask {

    private final int offset;

    private final TrackExecutionState trackExecutionState;
    private final Logger LOG;

    public Logger getLogger() {
        return LOG;
    }

    protected FogTaskBase(int offset, TrackExecutionState trackExecutionState, Class<?> clazz) {

        this.offset = offset;
        this.trackExecutionState = trackExecutionState;

        LOG = LoggerFactory.getLogger(clazz);
    }

    @Override
    public boolean shouldStart(DateTime simulationStart) {
        return Seconds.secondsBetween(simulationStart, new DateTime()).isGreaterThan(Seconds.seconds(offset));
    }

    @Override
    public boolean execute() {
        try {
            return internalExecute();
        } catch (Exception ex) {
            LOG.error("Task failed", ex);
            return false;
        }
    }

    @Override
    public boolean repeatOnError() {
        return true;
    }

    protected abstract boolean internalExecute();

    public TrackExecutionState getTrackExecutionState() {
        return trackExecutionState;
    }
}
