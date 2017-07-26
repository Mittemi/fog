package at.sintrum.fog.metadatamanager.service;

import at.sintrum.fog.metadatamanager.api.dto.MetadataBase;

import java.util.List;

/**
 * Created by Michael Mittermayr on 03.06.2017.
 */
public interface MetadataService<TModel extends MetadataBase> {

    TModel store(TModel metadata);

    TModel get(String fogId, String id);

    void delete(String fogId, String id);

    List<TModel> getAll(String fogId);
}
