package at.sintrum.fog.applicationhousing.service;

import at.sintrum.fog.applicationhousing.api.dto.AppInstanceIdHistoryInfo;

/**
 * Created by Michael Mittermayr on 02.09.2017.
 */
public interface InstanceIdHistoryService {
    void reset();

    boolean addToInstanceIdHistory(AppInstanceIdHistoryInfo historyInfo);

    void rollbackInstanceIdHistory(AppInstanceIdHistoryInfo historyInfo);

    String getLatestInstanceId(String instanceId);
}
