package andiag.coru.es.welegends.rest.entities.persist;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andyqm on 06/06/2016.
 */
public class ProfileIcon {

    private Integer id;
    private Image image;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
