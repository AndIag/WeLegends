package andiag.coru.es.welegends.entities;

import andiag.coru.es.welegends.entities.utils.Item;

/**
 * Created by andyq on 17/07/2015.
 */
public class ItemLeague implements Item {

    private League league;

    public ItemLeague(League league) {
        this.league = league;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    @Override
    public boolean isSection() {
        return false;
    }
}
