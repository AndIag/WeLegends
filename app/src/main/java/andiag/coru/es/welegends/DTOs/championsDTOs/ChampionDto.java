package andiag.coru.es.welegends.DTOs.championsDTOs;

import java.io.Serializable;

/**
 * Created by Iago on 25/07/2015.
 */
public class ChampionDto implements Serializable {

    private long id;
    private String name;
    private String key;
    private String title;

    public ChampionDto() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
