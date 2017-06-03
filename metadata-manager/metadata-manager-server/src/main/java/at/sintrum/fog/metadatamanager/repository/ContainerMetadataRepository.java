package at.sintrum.fog.metadatamanager.repository;

import at.sintrum.fog.metadatamanager.domain.DockerContainerMetadataEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Michael Mittermayr on 03.06.2017.
 */
public interface ContainerMetadataRepository extends CrudRepository<DockerContainerMetadataEntity, String> {

}
