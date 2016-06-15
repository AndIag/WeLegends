package andiag.coru.es.welegends.rest.entities.persist;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andyqm on 06/06/2016.
 */
public class Image {
    private String full;
    private String sprite;
    private String group;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
    }

    public String getSprite() {
        return sprite;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
