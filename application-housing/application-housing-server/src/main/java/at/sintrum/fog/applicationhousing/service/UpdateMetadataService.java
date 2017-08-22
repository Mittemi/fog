package at.sintrum.fog.applicationhousing.service;

import at.sintrum.fog.applicationhousing.api.dto.AppIdentification;
import at.sintrum.fog.applicationhousing.api.dto.AppUpdateInfo;

/**
 * Created by Michael Mittermayr on 17.07.2017.
 */
public interface UpdateMetadataService {

    void addUpdateMetadata(AppIdentification currentVersion, AppIdentification newVersion);

    AppUpdateInfo getUpdateInfo(AppIdentification currentVersion);

    void removeUpdate(AppIdentification appIdentification);

    void reset();
}
