package at.sintrum.fog.simulation.taskengine.tasks;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.core.dto.ResourceInfo;
import at.sintrum.fog.simulation.service.FogResourceService;
import at.sintrum.fog.simulation.taskengine.TaskListBuilder;

/**
 * Created by Michael Mittermayr on 03.09.2017.
 */
public class SetResourceLimitTask extends FogTaskBase {

    private final ResourceInfo resourceInfo;
    private final FogIdentification fogIdentification;
    private final FogResourceService fogResourceService;

    public SetResourceLimitTask(int offset, TaskListBuilder.TaskListBuilderState.AppTaskBuilder.TrackExecutionState trackExecutionState, ResourceInfo resourceInfo, FogIdentification fogIdentification, FogResourceService fogResourceService) {
        super(offset, trackExecutionState, SetResourceLimitTask.class);
        this.resourceInfo = resourceInfo;
        this.fogIdentification = fogIdentification;
        this.fogResourceService = fogResourceService;
    }

    @Override
    protected boolean internalExecute() {
        fogResourceService.setResourceRestriction(fogIdentification, resourceInfo);
        return true;
    }
}
