package at.sintrum.fog.metadatamanager.api;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.metadatamanager.api.dto.AppState;
import at.sintrum.fog.metadatamanager.api.dto.ApplicationStateMetadata;
import at.sintrum.fog.metadatamanager.service.ApplicationStateMetadataService;
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
    public void delete(@PathVariable("id") String id) {
        applicationStateMetadataService.delete(null, id);
    }

    @Override
    public List<ApplicationStateMetadata> getAll() {
        return applicationStateMetadataService.getAll(null);
    }

    @Override
    public ApplicationStateMetadata setState(String id, AppState state) {
        return applicationStateMetadataService.setState(id, state);
    }

    @Override
    public void reset() {
        applicationStateMetadataService.deleteAll();
    }

    @Override
    public List<ApplicationStateMetadata> getByFog(FogIdentification fogIdentification) {
        return applicationStateMetadataService.getManagedByFog(fogIdentification);
    }
}
