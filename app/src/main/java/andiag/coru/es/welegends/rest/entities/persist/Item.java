package andiag.coru.es.welegends.rest.entities.persist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by andyqm on 06/06/2016.
 */
public class Item {
    private String name;
    private String group;
    private String description;
    private String colloq;
    private String plaintext;
    private List<String> into = new ArrayList<String>();
    private Image image;
    //private Gold gold;
    private List<String> tags = new ArrayList<String>();
    //private Maps maps;
    //private Stats stats;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColloq() {
        return colloq;
    }

    public void setColloq(String colloq) {
        this.colloq = colloq;
    }

    public String getPlaintext() {
        return plaintext;
    }

    public void setPlaintext(String plaintext) {
        this.plaintext = plaintext;
    }

    public List<String> getInto() {
        return into;
    }

    public void setInto(List<String> into) {
        this.into = into;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

//    public Gold getGold() {
//        return gold;
//    }
//
//    public void setGold(Gold gold) {
//        this.gold = gold;
//    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

//    public Maps getMaps() {
//        return maps;
//    }
//
//    public void setMaps(Maps maps) {
//        this.maps = maps;
//    }
//
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
