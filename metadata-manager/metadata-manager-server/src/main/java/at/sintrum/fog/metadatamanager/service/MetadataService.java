package at.sintrum.fog.metadatamanager.service;

import at.sintrum.fog.metadatamanager.api.dto.MetadataBase;

import java.util.List;

/**
 * Created by Michael Mittermayr on 03.06.2017.
 */
public interface MetadataService<TModel extends MetadataBase> {

    TModel store(TModel metadata);

    TModel get(String id);

    void delete(String id);

    List<TModel> getAll();
}
