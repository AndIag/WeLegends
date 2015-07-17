package andiag.coru.es.welegends.entities;

import java.util.List;

/**
 * Created by andyq on 17/07/2015.
 */
public class Group {
    private String name;
    private List<League> leagues;

    public Group(String name, List<League> leagues) {
        this.name = name;
        this.leagues = leagues;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<League> getLeagues() {
        return leagues;
    }

    public void setLeagues(List<League> leagues) {
        this.leagues = leagues;
    }
}
