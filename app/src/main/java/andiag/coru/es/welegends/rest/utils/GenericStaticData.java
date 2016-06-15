package andiag.coru.es.welegends.rest.utils;

import java.util.HashMap;

/**
 * Created by Canalejas on 15/06/2016.
 */
public class GenericStaticData<K, E> {
    private String type;
    private String version;
    private String format;
    private HashMap<K, E> data;

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

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public HashMap<K, E> getData() {
        return data;
    }

    public void setData(HashMap<K, E> data) {
        this.data = data;
    }
}
