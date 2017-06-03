package at.sintrum.fog.metadatamanager.api;

import at.sintrum.fog.metadatamanager.api.dto.MetadataBase;
import at.sintrum.fog.metadatamanager.service.MetadataService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Created by Michael Mittermayr on 03.06.2017.
 */
public class MetadataControllerBase<TModel extends MetadataBase> implements MetadataApi<TModel> {

    private final MetadataService<TModel> modelMetadataService;

    MetadataControllerBase(MetadataService<TModel> modelMetadataService) {
        this.modelMetadataService = modelMetadataService;
    }


    @Override
    public TModel store(@RequestBody TModel metadata) {
        return modelMetadataService.store(metadata);
    }

    @Override
    public TModel getById(@PathVariable("id") String id) {
        return modelMetadataService.get(id);
    }

    @Override
    public List<TModel> getAll() {
        return modelMetadataService.getAll();
    }
}
