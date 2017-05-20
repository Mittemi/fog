package at.sintrum.fog.deploymentmanager.api.dto;

import org.springframework.util.StringUtils;

/**
 * Created by Michael Mittermayr on 20.05.2017.
 */
public class PushImageRequest {

    private String name;

    private String tag;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
