package andiag.coru.es.welegends.entities.DTOs;

import andiag.coru.es.welegends.entities.Summoner;

/**
 * Created by Iago on 26/06/2015.
 */
public class SummonerHistoryDto {

    private Summoner summoner;
    private long timestamp;
    private String region;

    public SummonerHistoryDto() {
    }

    public Summoner getSummoner() {
        return summoner;
    }

    public void setSummoner(Summoner summoner) {
        this.summoner = summoner;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
