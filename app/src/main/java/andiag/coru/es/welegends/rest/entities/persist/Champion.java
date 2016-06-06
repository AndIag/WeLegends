package andiag.coru.es.welegends.rest.entities.persist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by andyqm on 06/06/2016.
 */
public class Champion {
    private String version;
    private String id;
    private String key;
    private String name;
    private String title;
    private String blurb;
    //private Info info;
    private Image image;
    private List<String> tags = new ArrayList<String>();
    private String partype;
    //private Stats stats;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBlurb() {
        return blurb;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }

//    public Info getInfo() {
//        return info;
//    }
//
//    public void setInfo(Info info) {
//        this.info = info;
//    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getPartype() {
        return partype;
    }

    public void setPartype(String partype) {
        this.partype = partype;
    }

//    public Stats getStats() {
//        return stats;
//    }
//
//    public void setStats(Stats stats) {
//        this.stats = stats;
//    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
