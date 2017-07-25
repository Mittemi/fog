package at.sintrum.fog.metadatamanager.service;

import at.sintrum.fog.metadatamanager.api.dto.DockerImageMetadata;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * Created by Michael Mittermayr on 30.05.2017.
 */
@Service
public class ImageMetadataService extends RedissonMetadataServiceBase<DockerImageMetadata> {

    private static final Logger LOG = LoggerFactory.getLogger(ImageMetadataService.class);

    public ImageMetadataService(RedissonClient redissonClient) {
        super(redissonClient, DockerImageMetadata.class);
    }

    @Override
    String getOrGenerateId(DockerImageMetadata metadata) {

        if (StringUtils.isEmpty(metadata.getId())) {
            LOG.debug("Generate new Id for image metadata");
            metadata.setId(UUID.randomUUID().toString());
        }

        return metadata.getId();
    }

    @Override
    String getListName(String fogName) {
        return super.getListName("");
    }
}
