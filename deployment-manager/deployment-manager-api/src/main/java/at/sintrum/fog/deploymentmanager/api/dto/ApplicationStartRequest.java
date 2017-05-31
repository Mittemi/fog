package at.sintrum.fog.deploymentmanager.api.dto;

import org.springframework.util.StringUtils;

/**
 * Created by Michael Mittermayr on 31.05.2017.
 */
public class ApplicationStartRequest {

    private String imageId;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}
