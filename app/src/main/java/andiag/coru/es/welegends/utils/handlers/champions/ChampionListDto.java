package andiag.coru.es.welegends.utils.handlers.champions;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Iago on 25/07/2015.
 */
public class ChampionListDto implements Serializable {
    private HashMap<Integer, ChampionDto> data;
    private String type;
    private String version;

    //private String format;
    //private HashMap<String, String> keys;

    public ChampionListDto() {
    }

    public HashMap<Integer, ChampionDto> getData() {
        return data;
    }

    public void setData(HashMap<Integer, ChampionDto> data) {
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
