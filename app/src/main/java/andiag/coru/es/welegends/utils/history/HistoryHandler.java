package andiag.coru.es.welegends.utils.history;

import android.app.Activity;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import andiag.coru.es.welegends.DTOs.SummonerHistory;
import andiag.coru.es.welegends.entities.Summoner;

/**
 * Created by Iago on 26/06/2015.
 */
public abstract class HistoryHandler {

    private static final String HISTORY_FILE_NAME = "History";
    private static final int MAX_SUMMONERS_IN_HISTORY = 5;

    public static ArrayList<SummonerHistory> getHistory(Activity activity) throws JSONException {
        ArrayList<SummonerHistory> summoners = new ArrayList<>();
        Calendar limitTime = Calendar.getInstance();
        limitTime.add(Calendar.DAY_OF_MONTH, -2);
        long limit = limitTime.getTimeInMillis();

        SharedPreferences settings = activity.getSharedPreferences(HISTORY_FILE_NAME, 0);
        Map<String, String> map = (Map<String, String>) settings.getAll();

        if (map == null) {
            return new ArrayList<>();
        }

        Set<String> keys = map.keySet();

        Summoner sum;
        SummonerHistory summonerHistory;
        JSONObject mainObject;
        Calendar summonerTimeStamp;
        long timestamp;
        for (String s : keys) {
            sum = new Summoner();
            mainObject = new JSONObject(map.get(s));

            timestamp = mainObject.getLong("timestamp");

            if (timestamp > limit) {
                summonerHistory = new SummonerHistory();
                sum.setId(mainObject.getLong("id"));
                sum.setName(mainObject.getString("name"));
                sum.setProfileIconId(mainObject.getLong("profileIconId"));
                sum.setRevisionDate(mainObject.getLong("revisionDate"));
                sum.setSummonerLevel(mainObject.getInt("summonerLevel"));

                summonerHistory.setSummoner(sum);
                summonerHistory.setTimestamp(timestamp);
                summonerHistory.setRegion(mainObject.getString("region"));

                summoners.add(summonerHistory);
            }
        }

        Collections.sort(summoners, new Comparator<SummonerHistory>() {
            @Override
            public int compare(SummonerHistory lhs, SummonerHistory rhs) {
                if (lhs.getTimestamp() < rhs.getTimestamp()) {
                    return 1;
                }
                if (lhs.getTimestamp() > rhs.getTimestamp()) {
                    return -1;
                }
                return 0;
            }
        });

        return summoners;
    }

    public static void setHistory(Activity activity, ArrayList<SummonerHistory> summoners) throws JSONException {
        SharedPreferences settings = activity.getSharedPreferences(HISTORY_FILE_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        Calendar limitTime = Calendar.getInstance();
        limitTime.add(Calendar.DAY_OF_MONTH, -1);

        Collections.reverse(summoners);

        int count = 0;
        JSONObject jo;
        Summoner sum;
        long summonerTimeStamp;
        for (SummonerHistory summonerHistory : summoners) {
            if (count < MAX_SUMMONERS_IN_HISTORY) {
                jo = new JSONObject();
                sum = summonerHistory.getSummoner();
                summonerTimeStamp = summonerHistory.getTimestamp();

                jo.put("id", sum.getId());
                jo.put("name", sum.getName());
                jo.put("profileIconId", sum.getProfileIconId());
                jo.put("revisionDate", sum.getRevisionDate());
                jo.put("summonerLevel", sum.getSummonerLevel());
                jo.put("timestamp", summonerTimeStamp);

                jo.put("region", summonerHistory.getRegion());
                editor.putString(sum.getName(), jo.toString());

                count++;
            } else {
                break;
            }
        }
        editor.apply();
    }
}
