package andiag.coru.es.welegends.DTOs.recentGamesDTOs;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Iago on 17/07/2015.
 */
public class RecentGamesDto implements Serializable {

    private Integer summonerId;
    private ArrayList<GameDto> games;

    public RecentGamesDto() {
    }

    public Integer getSummonerId() {
        return summonerId;
    }

    public void setSummonerId(Integer summonerId) {
        this.summonerId = summonerId;
    }

    public ArrayList<GameDto> getGames() {
        return games;
    }

    public void setGames(ArrayList<GameDto> games) {
        this.games = games;
    }

}
