package andiag.coru.es.welegends.DTOs.summonerSpellsDTOs;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by iagoc on 27/08/2015.
 */
public class SummonerSpellListDto implements Serializable {
    private HashMap<Long, SummonerSpellDto> data;
    private String type;
    private String version;

    public SummonerSpellListDto() {
    }

    public HashMap<Long, SummonerSpellDto> getData() {
        return data;
    }

    public void setData(HashMap<Long, SummonerSpellDto> data) {
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
