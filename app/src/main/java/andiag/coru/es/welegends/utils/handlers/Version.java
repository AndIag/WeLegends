package andiag.coru.es.welegends.utils.handlers;

import android.app.Activity;
import android.content.SharedPreferences;

import org.json.JSONException;

import java.util.Map;

/**
 * Created by iagoc on 27/08/2015.
 */
public abstract class Version {
    private static final String FILE_NAME = "VersionData";
    private static String version;
    private static SharedPreferences settings;

    public static void setVersion(String c, Activity activity) throws JSONException {
        version = c;
        saveVersionInFile(activity);
    }

    public static String getVersion(Activity activity) throws JSONException {
        if (version == null) {
            retrieveVersionFromFile(activity);
        }
        if (version != null) {
            return version;
        }
        return "5.15.1";
    }

    private static void retrieveVersionFromFile(Activity activity) throws JSONException {
        settings = activity.getSharedPreferences(FILE_NAME, 0);

        Map<String, String> map = (Map<String, String>) settings.getAll();

        if (map == null) {
            return;
        }

        version = (map.get("version"));

    }

    private static void saveVersionInFile(Activity activity) throws JSONException {
        settings = activity.getSharedPreferences(FILE_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.clear();
        editor.apply();

        editor.putString("version", version);

        editor.apply();

    }

}
