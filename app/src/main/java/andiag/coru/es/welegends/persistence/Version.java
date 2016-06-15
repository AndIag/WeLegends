package andiag.coru.es.welegends.persistence;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by Canalejas on 15/06/2016.
 */
public class Version {

    private static final String FILE_NAME = "VersionData";
    private static final String ARG_VERSION = "Version";
    private static String version;

    public static void setVersion(String newVersion, Activity activity) {
        version = newVersion;

        SharedPreferences settings = activity.getSharedPreferences(FILE_NAME, 0);
        settings.edit().clear()
                .putString(ARG_VERSION, version)
                .apply();
    }

    public static String getVersion(Activity activity) {
        if (version == null) {
            SharedPreferences settings = activity.getSharedPreferences(FILE_NAME, 0);
            version = settings.getString(ARG_VERSION, "6.11.1");
        }

        return version;
    }

}
