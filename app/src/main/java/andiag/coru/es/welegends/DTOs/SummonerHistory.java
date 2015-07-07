package andiag.coru.es.welegends.DTOs;

import java.util.Calendar;

import andiag.coru.es.welegends.entities.Summoner;

/**
 * Created by Iago on 26/06/2015.
 */
public class SummonerHistory {

    private Summoner summoner;
    private Calendar timestamp;
    private String region;

    public SummonerHistory() {
    }

    public Summoner getSummoner() {
        return summoner;
    }

    public void setSummoner(Summoner summoner) {
        this.summoner = summoner;
    }

    public Calendar getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Calendar timestamp) {
        this.timestamp = timestamp;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
