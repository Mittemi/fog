package at.sintrum.fog.metadatamanager.service;

import at.sintrum.fog.metadatamanager.api.dto.MetadataBase;
import org.joda.time.DateTime;
import org.redisson.api.RMap;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael Mittermayr on 22.07.2017.
 */
public abstract class RedissonMetadataServiceBase<TModel extends MetadataBase> implements MetadataService<TModel> {

    private final String DEFAULT_FOG_NAME = "NoFogNameSet";

    private final RedissonClient redissonClient;
    private final Class<TModel> modelClazz;

    RedissonMetadataServiceBase(RedissonClient redissonClient, Class<TModel> modelClazz) {
        this.redissonClient = redissonClient;
        this.modelClazz = modelClazz;
    }

    @Override
    public TModel store(TModel metadata) {
        RMap<String, TModel> map = getMap(getFogName(metadata));

        TModel existing = map.getOrDefault(getOrGenerateId(metadata), null);

        if (existing != null) {
            metadata.setCreationDate(existing.getCreationDate());
        }
        if (metadata.getCreationDate() == null) {
            metadata.setCreationDate(new DateTime());
        }
        metadata.setLastUpdate(new DateTime());

        map.put(getOrGenerateId(metadata), metadata);
        return metadata;
    }

    private RMap<String, TModel> getMap(String fogName) {
        return redissonClient.getMap(getListName(fogName));
    }

    private RSet<String> getFogNames() {
        return redissonClient.getSet("Metadata.Map.Fogs");
    }

    String getListName(String fogName) {
        if (fogName == null) fogName = "";
        getFogNames().add(fogName);
        return "Metadata.Map." + fogName + modelClazz.getTypeName();
    }

    @Override
    public TModel get(String fogId, String id) {
        return getMap(fogId).get(id);
    }

    @Override
    public void delete(String fogId, String id) {
        getMap(fogId).remove(id);
    }

    public void deleteAll() {
        for (String name : getFogNames()) {
            getMap(name).clear();
        }
    }

    @Override
    public List<TModel> getAll(String fogId) {
        return new ArrayList<>(getMap(fogId).values());
    }

    abstract String getOrGenerateId(TModel model);

    String getFogName(TModel model) {
        return DEFAULT_FOG_NAME;
    }
}
