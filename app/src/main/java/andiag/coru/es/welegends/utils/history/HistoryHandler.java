package andiag.coru.es.welegends.utils.history;

import android.app.Activity;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import andiag.coru.es.welegends.entities.Summoner;

/**
 * Created by Iago on 26/06/2015.
 */
public abstract class HistoryHandler {

    private static final String historyFileName = "History";

    public static Map<String, Summoner> getHistory(Activity activity) throws JSONException {
        Map<String, Summoner> summoners = new HashMap<>();
        Calendar limitTime = Calendar.getInstance();
        limitTime.add(Calendar.DAY_OF_MONTH, -1);

        SharedPreferences settings = activity.getSharedPreferences(historyFileName, 0);
        Map<String, String> map = (Map<String, String>) settings.getAll();

        Set<String> keys = map.keySet();

        Summoner sum;
        for (String s : keys) {
            sum = new Summoner();
            JSONObject mainObject = new JSONObject(s);

            long timestamp = mainObject.getLong("timestamp");
            Calendar time = Calendar.getInstance();
            time.setTimeInMillis(timestamp);

            if (time.after(limitTime)) {
                sum.setId(mainObject.getLong("id"));
                sum.setName(mainObject.getString("name"));
                sum.setProfileIconId(mainObject.getLong("profileIconId"));
                sum.setRevisionDate(mainObject.getLong("revisionDate"));
                sum.setSummonerLevel(mainObject.getInt("summonerLevel"));
                summoners.put(s, sum);
            }
        }

        return summoners;
    }

    public static void setHistory(Activity activity, Map<String, Summoner> map) throws JSONException {
        SharedPreferences settings = activity.getSharedPreferences(historyFileName, 0);
        SharedPreferences.Editor editor = settings.edit();

        Set<String> keys = map.keySet();

        JSONObject jo;
        Summoner sum;
        for (String s : keys) {
            jo = new JSONObject();
            sum = map.get(s);

            jo.put("id", sum.getId());
            jo.put("name", sum.getName());
            jo.put("profileIconId", sum.getProfileIconId());
            jo.put("revisionDate", sum.getRevisionDate());
            jo.put("summonerLevel", sum.getSummonerLevel());
            jo.put("timestamp", Calendar.getInstance().getTimeInMillis());

            editor.putString(s, jo.toString());
        }
        editor.apply();
    }
}
