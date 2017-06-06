package at.sintrum.fog.metadatamanager.service;

import at.sintrum.fog.metadatamanager.api.dto.MetadataBase;
import at.sintrum.fog.metadatamanager.domain.BaseEntity;
import org.joda.time.DateTime;
import org.modelmapper.ModelMapper;
import org.springframework.data.repository.CrudRepository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by Michael Mittermayr on 03.06.2017.
 */
public abstract class MetadataServiceBase<TModel extends MetadataBase, TEntity extends BaseEntity, TRepository extends CrudRepository<TEntity, String>> implements MetadataService<TModel> {

    private TRepository repository;
    private ModelMapper modelMapper;
    private Class<TModel> modelClazz;
    private Class<TEntity> entityClass;

    MetadataServiceBase(TRepository repository, ModelMapper modelMapper, Class<TModel> modelClazz, Class<TEntity> entityClass) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.modelClazz = modelClazz;
        this.entityClass = entityClass;
    }

    public TModel store(TModel metadata) {

        TEntity map = modelMapper.map(metadata, entityClass);

        if (!StringUtils.isEmpty(getId(metadata))) {
            TEntity existingEntry = repository.findOne(getId(metadata));
            if (existingEntry != null) {
                map.setCreationDate(existingEntry.getCreationDate());
            }
        }
        if (map.getCreationDate() == null) {
            map.setCreationDate(new DateTime().toDate());
        }
        map.setLastUpdate(new DateTime().toDate());
        repository.save(map);
        return modelMapper.map(map, modelClazz);
    }

    public TModel get(String id) {
        TEntity one = repository.findOne(id);
        if (one == null) return null;
        return modelMapper.map(one, modelClazz);
    }

    public List<TModel> getAll() {
        List<TEntity> list = StreamSupport.stream(repository.findAll().spliterator(), false).collect(Collectors.toList());
        //return StreamSupport.stream(.spliterator(), false).map(x -> modelMapper.map(x, modelClazz)).collect(Collectors.toList());
        return list.stream().filter(Objects::nonNull).map(x -> modelMapper.map(x, modelClazz)).collect(Collectors.toList());
    }

    public void delete(String id) {
        repository.delete(id);
    }

    abstract String getId(TModel metadata);
}
