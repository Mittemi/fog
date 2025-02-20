package at.sintrum.fog.metadatamanager.api;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.metadatamanager.api.dto.AppState;
import at.sintrum.fog.metadatamanager.api.dto.ApplicationStateMetadata;
import at.sintrum.fog.metadatamanager.service.ApplicationStateMetadataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Michael Mittermayr on 01.08.2017.
 */
@RestController
public class ApplicationStateMetadataController implements ApplicationStateMetadataApi {

    private final ApplicationStateMetadataService applicationStateMetadataService;

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationStateMetadataController.class);

    public ApplicationStateMetadataController(ApplicationStateMetadataService applicationStateMetadataService) {
        this.applicationStateMetadataService = applicationStateMetadataService;
    }

    @Override
    public ApplicationStateMetadata store(@RequestBody ApplicationStateMetadata metadata) {
        return applicationStateMetadataService.store(metadata);
    }

    @Override
    public ApplicationStateMetadata getById(@PathVariable("id") String id) {
        return applicationStateMetadataService.get(null, id);
    }

    @Override
    public FogIdentification getApplicationUrl(@PathVariable("id") String id) {
        ApplicationStateMetadata state = getById(id);
        if (state == null || state.getRunningAt() == null) return null;
        return new FogIdentification(state.getRunningAt().getIp(), state.getPort());
    }

    @Override
    public void delete(@PathVariable("id") String id) {
        applicationStateMetadataService.delete(null, id);
    }

    @Override
    public List<ApplicationStateMetadata> getAll() {
        return applicationStateMetadataService.getAll(null);
    }

    @Override
    public ApplicationStateMetadata setState(@PathVariable("id") String id, @PathVariable("state") AppState state) {
        return applicationStateMetadataService.setState(id, state);
    }

    @Override
    public void reset() {
        applicationStateMetadataService.deleteAll();
    }

    @Override
    public List<ApplicationStateMetadata> getByFog(@RequestBody FogIdentification fogIdentification) {
        return applicationStateMetadataService.getManagedByFog(fogIdentification);
    }

    @Override
    public boolean deprecateInstance(@PathVariable("instanceId") String instanceId) {
        LOG.debug("Mark instance '" + instanceId + "' as deprecated");
        return applicationStateMetadataService.markInstanceAsDeprecated(instanceId);
    }

    @Override
    public boolean isActiveInstance(@PathVariable("instanceId") String instanceId) {
        LOG.debug("Check if instance '" + instanceId + "' is active");
        return !applicationStateMetadataService.isInstanceDeprecated(instanceId);
    }
}
