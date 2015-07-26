package andiag.coru.es.welegends.DTOs.championsDTOs;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Iago on 25/07/2015.
 */
public class ChampionListDto implements Serializable {
    private HashMap<Integer, ChampionDto> data;
    private String format;
    private HashMap<String, String> keys;
    private String type;
    private String version;

    public ChampionListDto() {
    }

    public HashMap<Integer, ChampionDto> getData() {
        return data;
    }

    public void setData(HashMap<Integer, ChampionDto> data) {
        this.data = data;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public HashMap<String, String> getKeys() {
        return keys;
    }

    public void setKeys(HashMap<String, String> keys) {
        this.keys = keys;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
