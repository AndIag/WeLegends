package andiag.coru.es.welegends.DTOs.summonerSpellsDTOs;

import java.io.Serializable;

/**
 * Created by iagoc on 27/08/2015.
 */
public class SummonerSpellDto implements Serializable {
    private long id;
    private String key;
    private String name;

    public SummonerSpellDto() {
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId(long id) {
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
}
