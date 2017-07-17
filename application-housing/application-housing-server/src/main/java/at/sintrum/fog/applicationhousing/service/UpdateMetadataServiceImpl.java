package at.sintrum.fog.applicationhousing.service;

import at.sintrum.fog.applicationhousing.api.dto.AppIdentification;
import at.sintrum.fog.applicationhousing.api.dto.AppUpdateInfo;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by Michael Mittermayr on 17.07.2017.
 */
@Service
public class UpdateMetadataServiceImpl implements UpdateMetadataService {

    private final RedissonClient redissonClient;

    public UpdateMetadataServiceImpl(RedissonClient redissonClient) {

        this.redissonClient = redissonClient;
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
            return new AppUpdateInfo(false, null);
        }
        return new AppUpdateInfo(true, newVersionMetadataId);
    }

    @Override
    public void removeUpdate(AppIdentification appIdentification) {
        getUpdateMetadata().remove(appIdentification.getImageMetadataId());
    }
}
