package andiag.coru.es.welegends.DTOs.rankedStatsDTOs;

import java.io.Serializable;

/**
 * Created by Iago on 07/07/2015.
 */
public class MiniSeriesDto implements Serializable {

    private int losses;
    private int wins;
    private String progress;
    private int target;

    public MiniSeriesDto() {
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

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }
}
