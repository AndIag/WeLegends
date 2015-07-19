package andiag.coru.es.welegends.entities;

import java.io.Serializable;

import andiag.coru.es.welegends.entities.utils.Item;

/**
 * Created by andyq on 17/07/2015.
 */
public class ItemSection implements Item, Serializable {

    private String name;

    public ItemSection(String name) {

        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isSection() {
        return true;
    }
}
