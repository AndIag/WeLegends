package andiag.coru.es.welegends.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by andyq on 16/07/2015.
 */
public class League implements Serializable {

    private String queue;
    private String name;
    private List<Entry> entries;
    private String tier;

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

}
