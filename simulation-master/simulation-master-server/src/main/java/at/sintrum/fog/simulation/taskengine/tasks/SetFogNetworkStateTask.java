package at.sintrum.fog.simulation.taskengine.tasks;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.simulation.service.FogCellStateService;
import at.sintrum.fog.simulation.taskengine.TrackExecutionState;

/**
 * Created by Michael Mittermayr on 05.09.2017.
 */
public class SetFogNetworkStateTask extends FogTaskBase {
    private final FogIdentification fogIdentification;
    private final boolean isOnline;
    private final boolean serviceOnly;
    private final FogCellStateService fogCellStateService;

    public SetFogNetworkStateTask(int offset, TrackExecutionState trackExecutionState, FogIdentification fogIdentification, boolean isOnline, boolean serviceOnly, FogCellStateService fogCellStateService) {
        super(offset, trackExecutionState, SetFogNetworkStateTask.class);
        this.fogIdentification = fogIdentification;
        this.isOnline = isOnline;
        this.serviceOnly = serviceOnly;
        this.fogCellStateService = fogCellStateService;
    }

    @Override
    protected boolean internalExecute() {
        if (serviceOnly) {
            fogCellStateService.setFogNetworkState(fogIdentification, isOnline);
        } else {
            fogCellStateService.setFogNetworkState(fogIdentification, isOnline);
        }
        return true;
    }
}
