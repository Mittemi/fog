package at.sintrum.fog.applicationhousing.service;

import at.sintrum.fog.applicationhousing.api.dto.AppInstanceIdHistoryInfo;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by Michael Mittermayr on 02.09.2017.
 */
@Service
public class InstanceIdHistoryServiceImpl implements InstanceIdHistoryService {

    private static final Logger LOG = LoggerFactory.getLogger(InstanceIdHistoryServiceImpl.class);

    private final RedissonClient redissonClient;

    public InstanceIdHistoryServiceImpl(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    private RMap<String, String> getMap() {
        return redissonClient.getMap("ApplicationHousing.InstanceID_Map");
    }

    @Override
    public void reset() {
        getMap().clear();
    }

    @Override
    public boolean addToInstanceIdHistory(AppInstanceIdHistoryInfo historyInfo) {
        getMap().put(historyInfo.getOldInstanceId(), historyInfo.getNewInstanceId());
        return true;
    }

    @Override
    public void rollbackInstanceIdHistory(AppInstanceIdHistoryInfo historyInfo) {
        getMap().remove(historyInfo.getOldInstanceId());
    }

    @Override
    public String getLatestInstanceId(String instanceId) {

        RMap<String, String> map = getMap();

        while (map.containsKey(instanceId)) {
            String newInstanceId = map.get(instanceId);
            if (StringUtils.isEmpty(newInstanceId)) {
                return instanceId;
            }
            instanceId = newInstanceId;
        }
        return instanceId;
    }
}
