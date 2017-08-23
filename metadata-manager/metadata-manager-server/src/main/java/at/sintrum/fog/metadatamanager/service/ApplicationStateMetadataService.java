package at.sintrum.fog.metadatamanager.service;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.metadatamanager.api.dto.AppState;
import at.sintrum.fog.metadatamanager.api.dto.ApplicationStateMetadata;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Michael Mittermayr on 01.08.2017.
 */
@Service
public class ApplicationStateMetadataService extends RedissonMetadataServiceBase<ApplicationStateMetadata> {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationStateMetadataService.class);

    public ApplicationStateMetadataService(RedissonClient redissonClient) {
        super(redissonClient, ApplicationStateMetadata.class);
    }

    @Override
    String getOrGenerateId(ApplicationStateMetadata metadata) {
        return metadata.getInstanceId();
    }

    @Override
    String getListName(String fogName) {
        return super.getListName("");
    }

    public ApplicationStateMetadata setState(String id, AppState state) {

        ApplicationStateMetadata stateMetadata = get(null, id);

        if (stateMetadata == null) {
            LOG.error("Unknown state metadata for instanceId: " + id);
            return null;
        }

        stateMetadata.setState(state);
        return store(stateMetadata);
    }

    public List<ApplicationStateMetadata> getManagedByFog(FogIdentification fogIdentification) {
        return getAll(null).stream().filter(x -> isStateManagedByFog(fogIdentification, x)).collect(Collectors.toList());
    }

    private boolean isStateManagedByFog(FogIdentification fog, ApplicationStateMetadata state) {
        return state.getRunningAt() != null && state.getRunningAt().isSameFog(fog) || state.getNextTarget() != null && (state.getState().equals(AppState.Moving) && state.getNextTarget().isSameFog(fog));
    }


    private RMap<String, Boolean> getInstanceDeprecatedMap() {
        return getRedissonClient().getMap("MetadataManager.InstanceDeprecated_Map");
    }

    public boolean markInstanceAsDeprecated(String instanceId) {
        getInstanceDeprecatedMap().put(instanceId, true);
        return true;
    }

    public boolean isInstanceDeprecated(String instanceId) {
        return getInstanceDeprecatedMap().getOrDefault(instanceId, false);
    }

    @Override
    public void deleteAll() {
        getInstanceDeprecatedMap().clear();
        super.deleteAll();
    }
}
