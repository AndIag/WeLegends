package andiag.coru.es.welegends.utils.handlers.champions;

import java.io.Serializable;

/**
 * Created by Iago on 25/07/2015.
 */
public class SkinDto implements Serializable {

    private int id;
    private String name;
    private int num;

    public SkinDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
