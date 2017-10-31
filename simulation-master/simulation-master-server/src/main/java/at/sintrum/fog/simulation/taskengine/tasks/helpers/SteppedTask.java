package at.sintrum.fog.simulation.taskengine.tasks.helpers;

import at.sintrum.fog.simulation.taskengine.TrackExecutionState;
import at.sintrum.fog.simulation.taskengine.tasks.FogTaskBase;

/**
 * Created by Michael Mittermayr on 31.10.2017.
 */
public class SteppedTask extends FogTaskBase {
    private final FogTaskBase taskA;
    private boolean aCompleted;
    private final boolean firstIsOptional;
    private final boolean tryFirstOnceOnly;
    private final FogTaskBase taskB;
    private final boolean secondIsOptional;
    private boolean firstRun = true;


    public SteppedTask(int offset, TrackExecutionState trackExecutionState, FogTaskBase taskA, boolean firstIsOptional, boolean tryFirstOnceOnly, FogTaskBase taskB, boolean secondIsOptional) {
        super(offset, trackExecutionState, SteppedTask.class);
        this.taskA = taskA;
        this.firstIsOptional = firstIsOptional;
        this.tryFirstOnceOnly = tryFirstOnceOnly;
        this.taskB = taskB;
        this.secondIsOptional = secondIsOptional;
    }

    @Override
    protected boolean internalExecute() {
        if (!aCompleted) {
            if (firstRun || !tryFirstOnceOnly) {
                aCompleted = taskA.execute();
            }
        }
        firstRun = false;

        if (aCompleted || firstIsOptional) {
            boolean result = taskB.execute();
            return result || secondIsOptional;
        }

        return false;
    }
}
