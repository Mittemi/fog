package at.sintrum.fog.applicationhousing.service;

import at.sintrum.fog.applicationhousing.api.dto.AppIdentification;
import at.sintrum.fog.applicationhousing.api.dto.AppUpdateInfo;
import at.sintrum.fog.metadatamanager.api.ImageMetadataApi;
import at.sintrum.fog.metadatamanager.api.dto.DockerImageMetadata;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by Michael Mittermayr on 17.07.2017.
 */
@Service
public class UpdateMetadataServiceImpl implements UpdateMetadataService {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateMetadataServiceImpl.class);

    private final RedissonClient redissonClient;
    private final ImageMetadataApi imageMetadataApi;

    public UpdateMetadataServiceImpl(RedissonClient redissonClient, ImageMetadataApi imageMetadataApi) {

        this.redissonClient = redissonClient;
        this.imageMetadataApi = imageMetadataApi;
    }

    private RMap<String, String> getUpdateMetadata() {
        return redissonClient.getMap("ApplicationHousing.AppUpdate_Map");
    }


    @Override
    public void addUpdateMetadata(AppIdentification currentVersion, AppIdentification newVersion) {
        getUpdateMetadata().put(currentVersion.getImageMetadataId(), newVersion.getImageMetadataId());
    }

    @Override
    public AppUpdateInfo getUpdateInfo(AppIdentification currentVersion) {

        String newVersionMetadataId = getUpdateMetadata().get(currentVersion.getImageMetadataId());

        if (StringUtils.isEmpty(newVersionMetadataId)) {
            String baseImageId = getBaseImageId(currentVersion);
            if (!currentVersion.getImageMetadataId().equals(baseImageId)) {
                newVersionMetadataId = getUpdateMetadata().get(baseImageId);
            }
        }

        if (StringUtils.isEmpty(newVersionMetadataId)) {
            return new AppUpdateInfo(false, null);
        }
        return new AppUpdateInfo(true, newVersionMetadataId);
    }

    private String getBaseImageId(AppIdentification currentVersion) {

        DockerImageMetadata currentImageMetadata = imageMetadataApi.getById(currentVersion.getImageMetadataId());

        if (currentImageMetadata == null) {
            LOG.error("Metadata not found for the currentVersion: " + currentVersion.getImageMetadataId());
            return currentVersion.getImageMetadataId();
        }

        if (StringUtils.isEmpty(currentImageMetadata.getBaseImageId())) {
            return currentVersion.getImageMetadataId();
        } else {
            // image is a checkpoint
            return currentImageMetadata.getBaseImageId();
        }
    }

    @Override
    public void removeUpdate(AppIdentification appIdentification) {
        getUpdateMetadata().remove(appIdentification.getImageMetadataId());
    }
}
