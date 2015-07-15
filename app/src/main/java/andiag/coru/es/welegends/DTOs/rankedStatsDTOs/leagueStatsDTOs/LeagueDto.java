package andiag.coru.es.welegends.DTOs.rankedStatsDTOs.leagueStatsDTOs;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Iago on 07/07/2015.
 */
public class LeagueDto implements Serializable {

    private ArrayList<LeagueEntryDto> entries;
    private String name;
    private String participantId; //RITO PLEASE QUE HACES CON TU VIDA
    private String queue;
    private String tier;

    public LeagueDto() {
    }

    public ArrayList<LeagueEntryDto> getEntries() {
        return entries;
    }

    public void setEntries(ArrayList<LeagueEntryDto> entries) {
        this.entries = entries;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }
}
