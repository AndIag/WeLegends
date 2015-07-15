package andiag.coru.es.welegends.DTOs.globalStatsDTOs;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Iago on 15/07/2015.
 */
public class PlayerStatsSummaryListDto implements Serializable {

    private ArrayList<PlayerStatsSummaryDto> playerStatSummaries;
    private long summonerId;

    public PlayerStatsSummaryListDto() {
    }

    public ArrayList<PlayerStatsSummaryDto> getPlayerStatSummaries() {
        return playerStatSummaries;
    }

    public void setPlayerStatSummaries(ArrayList<PlayerStatsSummaryDto> playerStatSummaries) {
        this.playerStatSummaries = playerStatSummaries;
    }

    public long getSummonerId() {
        return summonerId;
    }

    public void setSummonerId(long summonerId) {
        this.summonerId = summonerId;
    }
}
