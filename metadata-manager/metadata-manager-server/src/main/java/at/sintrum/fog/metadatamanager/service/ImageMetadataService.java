package at.sintrum.fog.metadatamanager.service;

import at.sintrum.fog.metadatamanager.api.dto.DockerImageMetadata;
import at.sintrum.fog.metadatamanager.domain.DockerImageMetadataEntity;
import at.sintrum.fog.metadatamanager.repository.ImageMetadataRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/**
 * Created by Michael Mittermayr on 30.05.2017.
 */
@Service
public class ImageMetadataService extends MetadataServiceBase<DockerImageMetadata, DockerImageMetadataEntity, ImageMetadataRepository> {

    public ImageMetadataService(ImageMetadataRepository repository, ModelMapper modelMapper) {
        super(repository, modelMapper, DockerImageMetadata.class, DockerImageMetadataEntity.class);
    }

    @Override
    String getId(DockerImageMetadata metadata) {
        return metadata.getId();
    }
}
