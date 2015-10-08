package andiag.coru.es.welegends.entities;

import java.io.Serializable;

/**
 * Created by Iago on 22/04/2015.
 */
public class BannedChampion implements Serializable {

    private int championId;
    private int pickTurn;
    private int teamId;

    public BannedChampion() {
    }

    public BannedChampion(int championId, int pickTurn) {
        this.championId = championId;
        this.pickTurn = pickTurn;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getChampionId() {
        return championId;
    }

    public void setChampionId(int championId) {
        this.championId = championId;
    }

    public int getPickTurn() {
        return pickTurn;
    }

    public void setPickTurn(int pickTurn) {
        this.pickTurn = pickTurn;
    }
}
