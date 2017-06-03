package at.sintrum.fog.metadatamanager.service;

import at.sintrum.fog.metadatamanager.api.dto.DockerContainerMetadata;
import at.sintrum.fog.metadatamanager.domain.DockerContainerMetadataEntity;
import at.sintrum.fog.metadatamanager.repository.ContainerMetadataRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/**
 * Created by Michael Mittermayr on 03.06.2017.
 */
@Service
public class ContainerMetadataService extends MetadataServiceBase<DockerContainerMetadata, DockerContainerMetadataEntity, ContainerMetadataRepository> {

    public ContainerMetadataService(ContainerMetadataRepository repository, ModelMapper modelMapper) {
        super(repository, modelMapper, DockerContainerMetadata.class, DockerContainerMetadataEntity.class);
    }

    @Override
    String getId(DockerContainerMetadata metadata) {
        return metadata.getContainerId();
    }
}
