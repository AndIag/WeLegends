package andiag.coru.es.welegends.DTOs.statsDTOs;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Iago on 07/07/2015.
 */
public class RankedStatsDto implements Serializable {

    private ArrayList<ChampionStatsDto> champions;
    private long modifyDate;
    private long summonerId;

    public RankedStatsDto() {
    }

    public ArrayList<ChampionStatsDto> getChampions() {
        return champions;
    }

    public void setChampions(ArrayList<ChampionStatsDto> champions) {
        this.champions = champions;
    }

    public long getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(long modifyDate) {
        this.modifyDate = modifyDate;
    }

    public long getSummonerId() {
        return summonerId;
    }

    public void setSummonerId(long summonerId) {
        this.summonerId = summonerId;
    }
}
