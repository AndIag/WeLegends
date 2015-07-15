package andiag.coru.es.welegends.DTOs.globalStatsDTOs;

import java.io.Serializable;

import andiag.coru.es.welegends.DTOs.rankedStatsDTOs.AggregatedStatsDto;

/**
 * Created by Iago on 15/07/2015.
 */
public class PlayerStatsSummaryDto implements Serializable {

    private AggregatedStatsDto aggregatedStatsDto;
    private int losses;
    private int wins;
    private long modifyDate;
    private String playerStatSummaryType;

    public PlayerStatsSummaryDto() {
    }

    public AggregatedStatsDto getAggregatedStatsDto() {
        return aggregatedStatsDto;
    }

    public void setAggregatedStatsDto(AggregatedStatsDto aggregatedStatsDto) {
        this.aggregatedStatsDto = aggregatedStatsDto;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public long getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(long modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getPlayerStatSummaryType() {
        return playerStatSummaryType;
    }

    public void setPlayerStatSummaryType(String playerStatSummaryType) {
        this.playerStatSummaryType = playerStatSummaryType;
    }
}
