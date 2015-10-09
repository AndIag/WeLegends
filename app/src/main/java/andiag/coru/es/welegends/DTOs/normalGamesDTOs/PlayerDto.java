package andiag.coru.es.welegends.DTOs.normalGamesDTOs;

import java.io.Serializable;

/**
 * Created by Iago on 17/07/2015.
 */
public class PlayerDto implements Serializable {

    private long summonerId;
    private int teamId;
    private int championId;

    public PlayerDto() {
    }

    public long getSummonerId() {
        return summonerId;
    }

    public void setSummonerId(long summonerId) {
        this.summonerId = summonerId;
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
}
