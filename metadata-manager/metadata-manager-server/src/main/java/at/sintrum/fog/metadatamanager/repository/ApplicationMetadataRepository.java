package at.sintrum.fog.metadatamanager.repository;

import at.sintrum.fog.metadatamanager.domain.DockerImageMetadataEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Michael Mittermayr on 30.05.2017.
 */
public interface ApplicationMetadataRepository extends CrudRepository<DockerImageMetadataEntity, String> {

}
