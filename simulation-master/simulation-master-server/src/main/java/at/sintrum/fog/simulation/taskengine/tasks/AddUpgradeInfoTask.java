package at.sintrum.fog.simulation.taskengine.tasks;

import at.sintrum.fog.applicationhousing.api.AppEvolutionApi;
import at.sintrum.fog.applicationhousing.api.dto.AppIdentification;
import at.sintrum.fog.applicationhousing.api.dto.AppUpdateMetadata;
import at.sintrum.fog.simulation.taskengine.TrackExecutionState;

/**
 * Created by Michael Mittermayr on 02.09.2017.
 */
public class AddUpgradeInfoTask extends FogTaskBase {

    private final AppEvolutionApi appEvolutionApi;
    private final AppIdentification oldVersion;
    private final AppIdentification newVersion;

    public AddUpgradeInfoTask(int offset, TrackExecutionState trackExecutionState, AppEvolutionApi appEvolutionApi, AppIdentification oldVersion, AppIdentification newVersion) {
        super(offset, trackExecutionState, AddUpgradeInfoTask.class);
        this.appEvolutionApi = appEvolutionApi;
        this.oldVersion = oldVersion;
        this.newVersion = newVersion;
    }

    @Override
    protected boolean internalExecute() {
        appEvolutionApi.setUpdateMetadata(new AppUpdateMetadata(oldVersion, newVersion));
        return true;
    }
}
