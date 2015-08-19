package andiag.coru.es.welegends.utils.handlers;

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

import andiag.coru.es.welegends.DTOs.SummonerHistoryDto;
import andiag.coru.es.welegends.entities.Summoner;

/**
 * Created by Iago on 26/06/2015.
 */
public abstract class SummonerHistory {

    private static final String HISTORY_FILE_NAME = "History";
    private static final int MAX_SUMMONERS_IN_HISTORY = 5;

    public static ArrayList<SummonerHistoryDto> getHistory(Activity activity) throws JSONException {
        ArrayList<SummonerHistoryDto> summoners = new ArrayList<>();
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
        SummonerHistoryDto summonerHistoryDto;
        JSONObject mainObject;
        long timestamp;
        for (String s : keys) {
            sum = new Summoner();
            mainObject = new JSONObject(map.get(s));

            timestamp = mainObject.getLong("timestamp");

            if (timestamp > limit) {
                summonerHistoryDto = new SummonerHistoryDto();
                sum.setId(mainObject.getLong("id"));
                sum.setName(mainObject.getString("name"));
                sum.setProfileIconId(mainObject.getLong("profileIconId"));
                sum.setRevisionDate(mainObject.getLong("revisionDate"));
                sum.setSummonerLevel(mainObject.getInt("summonerLevel"));

                summonerHistoryDto.setSummoner(sum);
                summonerHistoryDto.setTimestamp(timestamp);
                summonerHistoryDto.setRegion(mainObject.getString("region"));

                summoners.add(summonerHistoryDto);
            }
        }

        Collections.sort(summoners, new Comparator<SummonerHistoryDto>() {
            @Override
            public int compare(SummonerHistoryDto lhs, SummonerHistoryDto rhs) {
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

    public static void setHistory(Activity activity, ArrayList<SummonerHistoryDto> summoners) throws JSONException {
        SharedPreferences settings = activity.getSharedPreferences(HISTORY_FILE_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        Calendar limitTime = Calendar.getInstance();
        limitTime.add(Calendar.DAY_OF_MONTH, -1);

        Collections.sort(summoners, new Comparator<SummonerHistoryDto>() {
            @Override
            public int compare(SummonerHistoryDto lhs, SummonerHistoryDto rhs) {
                if (lhs.getTimestamp() < rhs.getTimestamp()) {
                    return 1;
                }
                if (lhs.getTimestamp() > rhs.getTimestamp()) {
                    return -1;
                }
                return 0;
            }
        });

        int count = 0;
        JSONObject jo;
        Summoner sum;
        long summonerTimeStamp;
        for (SummonerHistoryDto summonerHistoryDto : summoners) {
            if (count < MAX_SUMMONERS_IN_HISTORY) {
                jo = new JSONObject();
                sum = summonerHistoryDto.getSummoner();
                summonerTimeStamp = summonerHistoryDto.getTimestamp();

                jo.put("id", sum.getId());
                jo.put("name", sum.getName());
                jo.put("profileIconId", sum.getProfileIconId());
                jo.put("revisionDate", sum.getRevisionDate());
                jo.put("summonerLevel", sum.getSummonerLevel());
                jo.put("timestamp", summonerTimeStamp);

                jo.put("region", summonerHistoryDto.getRegion());
                editor.putString(sum.getName(), jo.toString());

                count++;
            } else {
                editor.remove(summonerHistoryDto.getSummoner().getName());
            }
        }
        editor.apply();
    }
}
