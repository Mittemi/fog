package at.sintrum.fog.simulation.service;

import at.sintrum.fog.core.dto.FogIdentification;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Michael Mittermayr on 30.08.2017.
 */
@Service
public class FogCellStateServiceImpl implements FogCellStateService {

    private final ConcurrentHashMap<String, Boolean> offlineMap;

    public FogCellStateServiceImpl() {
        offlineMap = new ConcurrentHashMap<>();
    }


    @Override
    public boolean isOnline(FogIdentification fogIdentification) {

        if (offlineMap.getOrDefault(fogIdentification.toFogId(), false))
            return false;

        if (offlineMap.getOrDefault(fogIdentification.getIp(), false))
            return false;

        return true;
    }

    @Override
    public void setFogNetworkState(FogIdentification fogIdentification, boolean isOnline) {
        if (isOnline) {
            offlineMap.remove(fogIdentification.toFogId());
            offlineMap.remove(fogIdentification.getIp());
        } else {
            offlineMap.put(fogIdentification.toFogId(), true);
            offlineMap.put(fogIdentification.getIp(), true);
        }
    }

    @Override
    public void setFogServiceState(FogIdentification fogIdentification, boolean isOnline) {
        if (isOnline) {
            offlineMap.remove(fogIdentification.toFogId());
        } else {
            offlineMap.put(fogIdentification.toFogId(), true);
        }
    }

    @Override
    public void reset() {
        offlineMap.clear();
    }
}
