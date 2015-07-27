package andiag.coru.es.welegends.utils.static_data;

import andiag.coru.es.welegends.DTOs.championsDTOs.ChampionListDto;

/**
 * Created by Iago on 25/07/2015.
 */
public abstract class ChampionsHandler {
    private static ChampionListDto champions;

    public static ChampionListDto getChampions() {
        return champions;
    }

    public static void setChampions(ChampionListDto c) {
        champions = c;
    }

    public static String getChampName(int id) {
        if (champions == null) return "NOT FOUND";
        return champions.getData().get(id).getName();
    }

}
