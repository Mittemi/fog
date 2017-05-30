package at.sintrum.fog.metadatamanager.service;

import at.sintrum.fog.metadatamanager.api.dto.DockerImageMetadata;
import at.sintrum.fog.metadatamanager.domain.DockerImageMetadataEntity;
import at.sintrum.fog.metadatamanager.repository.ApplicationMetadataRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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

    public void storeMetdata(DockerImageMetadata metadata) {

        DockerImageMetadataEntity map = modelMapper.map(metadata, DockerImageMetadataEntity.class);
        repository.save(map);
    }

    public DockerImageMetadata getMetadata(String imageId) {
        DockerImageMetadataEntity one = repository.findOne(imageId);
        return modelMapper.map(one, DockerImageMetadata.class);
    }
}
