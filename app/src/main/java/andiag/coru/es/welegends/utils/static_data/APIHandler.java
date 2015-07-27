package andiag.coru.es.welegends.utils.static_data;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Iago on 26/06/2015.
 */
public class APIHandler {

    private static final String APIConf_FILE_NAME = "api_conf.properties";
    private static APIHandler ourInstance;

    private String server;
    private String matchHistory;
    private String match;
    private String summoner;
    private String icon;
    private String stats;
    private String leagues;
    private String recent_games;
    private String champions;
    private String versions;

    private APIHandler(Activity activity) {

        Resources resources = activity.getResources();
        AssetManager assetManager = resources.getAssets();
        // Read from the /assets directory
        try {
            InputStream inputStream = assetManager.open(APIConf_FILE_NAME);
            Properties properties = new Properties();
            properties.load(inputStream);

            server = properties.getProperty("server");
            matchHistory = properties.getProperty("matchHistory");
            match = properties.getProperty("match");
            summoner = properties.getProperty("summoner");
            icon = properties.getProperty("icon");
            stats = properties.getProperty("stats");
            leagues = properties.getProperty("leagues");
            recent_games = properties.getProperty("recent_games");
            champions = properties.getProperty("champions");
            versions = properties.getProperty("versions");

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static APIHandler getInstance() {
        if (ourInstance == null) {
            return null;
        }
        return ourInstance;
    }

    public static APIHandler getInstance(Activity activity) {
        if (ourInstance == null) {
            ourInstance = new APIHandler(activity);
        }
        return ourInstance;
    }


    public String getVersions() {
        return versions;
    }

    public String getServer() {
        return server;
    }

    public String getMatchHistory() {
        return matchHistory;
    }

    public String getMatch() {
        return match;
    }

    public String getSummoner() {
        return summoner;
    }

    public String getIcon() {
        return icon;
    }

    public String getStats() {
        return stats;
    }

    public String getLeagues() {
        return leagues;
    }

    public String getRecent_games() {
        return recent_games;
    }

    public String getChampions() {
        return champions;
    }
}
