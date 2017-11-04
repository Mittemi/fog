package at.sintrum.fog.metadatamanager.service.requests;

import at.sintrum.fog.metadatamanager.api.dto.AppRequestInfo;

import java.util.Comparator;

/**
 * Created by Michael Mittermayr on 28.10.2017.
 */
public class SortByDateComparator implements Comparator<AppRequestInfo> {
    @Override
    public int compare(AppRequestInfo o1, AppRequestInfo o2) {
        return o1.getCreationDate().compareTo(o2.getCreationDate());
    }
}
