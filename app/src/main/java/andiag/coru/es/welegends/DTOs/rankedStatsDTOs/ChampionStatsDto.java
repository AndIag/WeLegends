package andiag.coru.es.welegends.DTOs.rankedStatsDTOs;

import java.io.Serializable;

/**
 * Created by Iago on 07/07/2015.
 */
public class ChampionStatsDto implements Serializable {

    private int id; //Note that champion ID 0 represents the combined stats for all champions
    private AggregatedStatsDto stats;

    public ChampionStatsDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AggregatedStatsDto getStats() {
        return stats;
    }

    public void setStats(AggregatedStatsDto stats) {
        this.stats = stats;
    }
}
