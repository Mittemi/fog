package at.sintrum.fog.metadatamanager.service;

import at.sintrum.fog.metadatamanager.api.dto.DockerContainerMetadata;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by Michael Mittermayr on 03.06.2017.
 */
@Service
public class ContainerMetadataService extends RedissonMetadataServiceBase<DockerContainerMetadata> {

    private static final Logger LOG = LoggerFactory.getLogger(ContainerMetadataService.class);

    ContainerMetadataService(RedissonClient redissonClient) {
        super(redissonClient, DockerContainerMetadata.class);
    }

    @Override
    String getOrGenerateId(DockerContainerMetadata dockerContainerMetadata) {

        if (StringUtils.isEmpty(dockerContainerMetadata.getContainerId())) {
            LOG.error("ContainerId required. This field must not be null.");
        }

        return dockerContainerMetadata.getContainerId();
    }
}
