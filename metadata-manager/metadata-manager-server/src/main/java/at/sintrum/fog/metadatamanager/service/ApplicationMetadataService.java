package at.sintrum.fog.metadatamanager.service;

import at.sintrum.fog.metadatamanager.api.dto.DockerImageMetadata;
import at.sintrum.fog.metadatamanager.domain.DockerImageMetadataEntity;
import at.sintrum.fog.metadatamanager.repository.ApplicationMetadataRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by Michael Mittermayr on 30.05.2017.
 */
@Service
public class ApplicationMetadataService {

    private ApplicationMetadataRepository repository;
    private ModelMapper modelMapper;

    public ApplicationMetadataService(ApplicationMetadataRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    public void storeMetadata(DockerImageMetadata metadata) {

        DockerImageMetadataEntity map = modelMapper.map(metadata, DockerImageMetadataEntity.class);
        repository.save(map);
    }

    public DockerImageMetadata getMetadata(String imageId) {
        DockerImageMetadataEntity one = repository.findOne(imageId);
        if (one == null) return null;
        return modelMapper.map(one, DockerImageMetadata.class);
    }

    public List<DockerImageMetadata> getAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).map(x -> modelMapper.map(x, DockerImageMetadata.class)).collect(Collectors.toList());
    }
}
