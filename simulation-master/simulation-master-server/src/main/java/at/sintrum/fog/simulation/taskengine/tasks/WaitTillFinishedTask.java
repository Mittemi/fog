package at.sintrum.fog.simulation.taskengine.tasks;

import at.sintrum.fog.applicationhousing.client.api.AppEvolutionClient;
import at.sintrum.fog.simulation.taskengine.TrackExecutionState;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Michael Mittermayr on 31.10.2017.
 */
public class WaitTillFinishedTask extends FogTaskBase {
    private final AppEvolutionClient appEvolutionClient;
    private final State state;

    public WaitTillFinishedTask(int offset, TrackExecutionState trackExecutionState, AppEvolutionClient appEvolutionClient, State state) {
        super(offset, trackExecutionState, WaitTillFinishedTask.class);
        this.appEvolutionClient = appEvolutionClient;
        this.state = state;
    }

    @Override
    protected boolean internalExecute() {

        updateInstanceId(appEvolutionClient, getTrackExecutionState());

        return state.isAllRequestsCompleted();
    }

    public static class State {

        private AtomicInteger runningApplications = new AtomicInteger(0);

        private boolean allRequestsCompleted;

        public boolean isAllRequestsCompleted() {
            return allRequestsCompleted;
        }

        public void setAllRequestsCompleted(boolean allRequestsCompleted) {
            this.allRequestsCompleted = allRequestsCompleted;
        }

        public void appStarted() {
            runningApplications.incrementAndGet();
        }

        public void appStopped() {
            runningApplications.decrementAndGet();
        }

        public int getRunningApplications() {
            return runningApplications.get();
        }
    }
}
