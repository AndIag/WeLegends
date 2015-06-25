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
    private String summonerByName;

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
            summonerByName = properties.getProperty("summonerByName");

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static APIHandler getInstance(Activity activity) {
        if (ourInstance == null) {
            return new APIHandler(activity);
        }
        return ourInstance;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getMatchHistory() {
        return matchHistory;
    }

    public void setMatchHistory(String matchHistory) {
        this.matchHistory = matchHistory;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getSummonerByName() {
        return summonerByName;
    }

    public void setSummonerByName(String summonerByName) {
        this.summonerByName = summonerByName;
    }
}
