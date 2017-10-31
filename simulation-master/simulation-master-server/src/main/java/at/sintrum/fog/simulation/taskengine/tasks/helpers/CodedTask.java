package at.sintrum.fog.simulation.taskengine.tasks.helpers;

import at.sintrum.fog.simulation.taskengine.TrackExecutionState;
import at.sintrum.fog.simulation.taskengine.tasks.FogTaskBase;

import java.util.function.Supplier;

/**
 * Created by Michael Mittermayr on 31.10.2017.
 */
public class CodedTask extends FogTaskBase {
    private final Supplier<Boolean> function;

    public CodedTask(int offset, TrackExecutionState trackExecutionState, Supplier<Boolean> function) {
        super(offset, trackExecutionState, CodedTask.class);
        this.function = function;
    }

    @Override
    protected boolean internalExecute() {
        return function.get();
    }
}
