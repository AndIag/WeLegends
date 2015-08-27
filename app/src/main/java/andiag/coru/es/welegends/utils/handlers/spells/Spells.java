package andiag.coru.es.welegends.utils.handlers.spells;

import android.app.Activity;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


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
        HashMap<Integer, SummonerSpellDto> hash = new HashMap<>();
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

            hash.put(Integer.valueOf(s), spellDto);
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
        for (Integer i : spells.getData().keySet()) {
            spellDto = spells.getData().get(i);
            jsonObject = new JSONObject();

            jsonObject.put("id", spellDto.getId());
            jsonObject.put("name", spellDto.getName());
            jsonObject.put("key", spellDto.getKey());

            editor.putString(Integer.toString(i), jsonObject.toString());
        }
        editor.apply();

    }

    public static String getServerVersion() {
        if (spells != null && spells.getVersion() != null) {
            return spells.getVersion();
        }
        return "5.15.1";
    }

    public static String getSpellName(int id) {
        if (spells == null) return "NOT FOUND";
        if (spells.getData() == null) return "NOT FOUNT";
        if (spells.getData().get(id) == null) return "NOT FOUND";
        return spells.getData().get(id).getName();
    }

    public static String getSpellKey(int id) {
        if (spells == null) return "Null";
        if (spells.getData() == null) return "Null";
        if (spells.getData().get(id) == null) return "Null";
        return spells.getData().get(id).getKey();
    }

}
