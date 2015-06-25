package andiag.coru.es.welegends.entities;

import java.io.Serializable;

/**
 * Created by Iago on 23/04/2015.
 */
public class Mastery implements Serializable {

    private long masteryId;
    private long rank;

    public Mastery() {
    }

    public Mastery(long masteryId, long rank) {
        this.masteryId = masteryId;
        this.rank = rank;
    }

    public long getRank() {
        return rank;
    }

    public void setRank(long rank) {
        this.rank = rank;
    }

    public long getMasteryId() {
        return masteryId;
    }

    public void setMasteryId(long masteryId) {
        this.masteryId = masteryId;
    }
}
