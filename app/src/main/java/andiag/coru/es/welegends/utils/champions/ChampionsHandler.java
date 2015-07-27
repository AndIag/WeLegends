package andiag.coru.es.welegends.utils.champions;

import android.app.Activity;
import android.content.SharedPreferences;

import andiag.coru.es.welegends.DTOs.championsDTOs.ChampionListDto;

/**
 * Created by Iago on 25/07/2015.
 */
public abstract class ChampionsHandler {
    private static final String HISTORY_FILE_NAME = "ChampionsData";
    private static ChampionListDto champions;

    public static ChampionListDto getChampions() {
        return champions;
    }

    public static void setChampions(ChampionListDto c) {
        if (c != null) {
            champions = c;
            saveChampionsInFile();
            return;
        }
        champions = retrieveChampionsFromFile();
    }

    private static ChampionListDto retrieveChampionsFromFile() {
        return null;
    }

    private static void saveChampionsInFile() {

    }

    public static String getVersion(Activity activity) {
        SharedPreferences settings = activity.getSharedPreferences(HISTORY_FILE_NAME, 0);
        return settings.getString("version", "0");
    }

    public static String getChampName(int id) {
        if (champions == null) return "NOT FOUND";
        return champions.getData().get(id).getName();
    }

}
