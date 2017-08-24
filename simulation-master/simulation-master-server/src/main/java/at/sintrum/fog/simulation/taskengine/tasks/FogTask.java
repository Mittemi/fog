package at.sintrum.fog.simulation.taskengine.tasks;

import org.joda.time.DateTime;

/**
 * Created by Michael Mittermayr on 24.08.2017.
 */
public interface FogTask {

    boolean shouldStart(DateTime simulationStart);

    boolean repeatOnError();

    boolean execute();
}
