package andiag.coru.es.welegends.entities;

/**
 * Created by andyq on 17/07/2015.
 */
public class ItemSection implements Item {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemSection(String name) {

        this.name = name;
    }


    @Override
    public boolean isSection() {
        return true;
    }
}
