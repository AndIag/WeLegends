package andiag.coru.es.welegends.entities;

import java.io.Serializable;

/**
 * Created by Iago on 23/04/2015.
 */
public class Rune implements Serializable {

    private long runeId;
    private long rank;

    public Rune() {
    }

    public Rune(long runeId, long rank) {
        this.runeId = runeId;
        this.rank = rank;
    }

    public long getRuneId() {
        return runeId;
    }

    public void setRuneId(long runeId) {
        this.runeId = runeId;
    }

    public long getRank() {
        return rank;
    }

    public void setRank(long rank) {
        this.rank = rank;
    }
}