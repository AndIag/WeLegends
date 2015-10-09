package andiag.coru.es.welegends.utils.static_data;

import android.app.Activity;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import andiag.coru.es.welegends.DTOs.summonerSpellsDTOs.SummonerSpellDto;
import andiag.coru.es.welegends.DTOs.summonerSpellsDTOs.SummonerSpellListDto;


/**
 * Created by iagoc on 27/08/2015.
 */
public abstract class Spells {
    private static final String FILE_NAME = "SpellsData";
    private static SummonerSpellListDto spells;
    private static SharedPreferences settings;

    public static void setSpells(SummonerSpellListDto c, Activity activity) throws JSONException {
        if (c != null) {
            spells = c;
            saveSpellsInFile(activity);
            return;
        }
        retrieveSpellsFromFile(activity);
    }

    private static void retrieveSpellsFromFile(Activity activity) throws JSONException {
        HashMap<Long, SummonerSpellDto> hash = new HashMap<>();
        spells = new SummonerSpellListDto();
        settings = activity.getSharedPreferences(FILE_NAME, 0);

        Map<String, String> map = (Map<String, String>) settings.getAll();

        if (map == null) {
            return;
        }

        Set<String> keys = map.keySet();

        spells.setType(map.get("type"));
        spells.setVersion(map.get("version"));

        SummonerSpellDto spellDto;
        JSONObject jsonObject;
        for (String s : keys) {
            if (s.equals("type") || s.equals("version")) {
                continue;
            }
            jsonObject = new JSONObject(map.get(s));
            spellDto = new SummonerSpellDto();

            spellDto.setId(jsonObject.getInt("id"));
            spellDto.setName(jsonObject.getString("name"));
            spellDto.setKey(jsonObject.getString("key"));

            hash.put(Long.valueOf(s), spellDto);
        }

        spells.setData(hash);
    }

    private static void saveSpellsInFile(Activity activity) throws JSONException {
        settings = activity.getSharedPreferences(FILE_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.clear();
        editor.apply();

        editor.putString("type", spells.getType());
        editor.putString("version", spells.getVersion());

        SummonerSpellDto spellDto;
        JSONObject jsonObject;
        for (Long i : spells.getData().keySet()) {
            spellDto = spells.getData().get(i);
            jsonObject = new JSONObject();

            jsonObject.put("id", spellDto.getId());
            jsonObject.put("name", spellDto.getName());
            jsonObject.put("key", spellDto.getKey());

            editor.putString(Long.toString(i), jsonObject.toString());
        }
        editor.apply();

    }

    public static String getServerVersion() {
        if (spells != null && spells.getVersion() != null) {
            return spells.getVersion();
        }
        return "5.15.1";
    }

    public static String getSpellName(long id) {
        if (spells == null) return "NOT FOUND";
        if (spells.getData() == null) return "NOT FOUNT";
        if (spells.getData().get(id) == null) return "NOT FOUND";
        return spells.getData().get(id).getName();
    }

    public static String getSpellKey(long id) {
        if (spells == null) return "Null";
        if (spells.getData() == null) return "Null";
        if (spells.getData().get(id) == null) return "Null";
        return spells.getData().get(id).getKey();
    }

}
