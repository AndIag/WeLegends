package andiag.coru.es.welegends.utils.champions;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import andiag.coru.es.welegends.DTOs.championsDTOs.ChampionDto;
import andiag.coru.es.welegends.DTOs.championsDTOs.ChampionListDto;

/**
 * Created by Iago on 25/07/2015.
 */
public abstract class ChampionsHandler {
    private static final String HISTORY_FILE_NAME = "ChampionsData";
    private static ChampionListDto champions;
    private static SharedPreferences settings;

    public static ChampionListDto getChampions() {
        return champions;
    }

    public static void setChampions(ChampionListDto c, Activity activity) throws JSONException {
        if (c != null) {
            Log.d("DINAMIC", "DINAMIC LOAD");
            champions = c;
            saveChampionsInFile(activity);
            return;
        }
        Log.d("STATIC", "STATIC LOAD");
        retrieveChampionsFromFile(activity);
    }

    private static void retrieveChampionsFromFile(Activity activity) throws JSONException {
        HashMap<Integer, ChampionDto> hash = new HashMap<>();
        champions = new ChampionListDto();
        settings = activity.getSharedPreferences(HISTORY_FILE_NAME, 0);

        Map<String, String> map = (Map<String, String>) settings.getAll();

        if (map == null) {
            return;
        }

        Set<String> keys = map.keySet();

        champions.setType(map.get("type"));
        champions.setVersion(map.get("version"));

        ChampionDto championDto;
        JSONObject jsonObject;
        for (String s : keys) {
            if (s.equals("type") || s.equals("version")) {
                continue;
            }
            jsonObject = new JSONObject(map.get(s));
            championDto = new ChampionDto();

            championDto.setId(jsonObject.getInt("id"));
            championDto.setName(jsonObject.getString("name"));
            championDto.setKey(jsonObject.getString("key"));
            championDto.setTitle(jsonObject.getString("title"));

            hash.put(Integer.valueOf(s), championDto);
        }

        champions.setData(hash);
    }

    private static void saveChampionsInFile(Activity activity) throws JSONException {
        settings = activity.getSharedPreferences(HISTORY_FILE_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.clear();
        editor.apply();

        editor.putString("type", champions.getType());
        editor.putString("version", champions.getVersion());

        ChampionDto championDto;
        JSONObject jsonObject;
        for (Integer i : champions.getData().keySet()) {
            championDto = champions.getData().get(i);
            jsonObject = new JSONObject();

            jsonObject.put("id", championDto.getId());
            jsonObject.put("name", championDto.getName());
            jsonObject.put("key", championDto.getKey());
            jsonObject.put("title", championDto.getTitle());

            editor.putString(Integer.toString(i), jsonObject.toString());
        }
        editor.apply();

    }

    public static String getServerVersion(Activity activity) {
        settings = activity.getSharedPreferences(HISTORY_FILE_NAME, 0);
        return settings.getString("version", "0");
    }

    public static String getChampName(int id) {
        if (champions == null) return "NOT FOUND";
        return champions.getData().get(id).getName();
    }

}
