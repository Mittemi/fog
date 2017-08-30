package at.sintrum.fog.servercore.connectionfilter;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.core.service.EnvironmentInfoService;
import at.sintrum.fog.simulation.api.FogCellStateApi;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.springframework.stereotype.Component;

/**
 * Created by Michael Mittermayr on 30.08.2017.
 */
@Component
public class ServiceStateInfoServiceImpl implements ServiceStateInfoService {

    private final EnvironmentInfoService environmentInfoService;
    private final FogCellStateApi fogCellStateApi;

    public ServiceStateInfoServiceImpl(EnvironmentInfoService environmentInfoService, FogCellStateApi fogCellStateApi) {
        this.environmentInfoService = environmentInfoService;
        this.fogCellStateApi = fogCellStateApi;

    }

    private boolean state;
    private DateTime lastUpdate;

    @Override
    public boolean isOnlineFor(FogIdentification fogIdentification) {

        if (lastUpdate == null || Seconds.secondsBetween(lastUpdate, new DateTime()).isGreaterThan(Seconds.seconds(30))) {
            lastUpdate = new DateTime();
            state = fogCellStateApi.isOnline(FogIdentification.parseFogBaseUrl(environmentInfoService.getFogBaseUrl()));
        }

        return state;
    }
}
