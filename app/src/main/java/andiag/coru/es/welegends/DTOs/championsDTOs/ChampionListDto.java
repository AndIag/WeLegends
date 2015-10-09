package andiag.coru.es.welegends.DTOs.championsDTOs;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Iago on 25/07/2015.
 */
public class ChampionListDto implements Serializable {
    private HashMap<Long, ChampionDto> data;
    private String type;
    private String version;

    public ChampionListDto() {
    }

    public HashMap<Long, ChampionDto> getData() {
        return data;
    }

    public void setData(HashMap<Long, ChampionDto> data) {
        this.data = data;
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
