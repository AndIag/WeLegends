package andiag.coru.es.welegends.utils.handlers.spells;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by iagoc on 27/08/2015.
 */
public class SummonerSpellListDto implements Serializable {
    private HashMap<Integer, SummonerSpellDto> data;
    private String type;
    private String version;

    public SummonerSpellListDto() {
    }

    public HashMap<Integer, SummonerSpellDto> getData() {
        return data;
    }

    public void setData(HashMap<Integer, SummonerSpellDto> data) {
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
