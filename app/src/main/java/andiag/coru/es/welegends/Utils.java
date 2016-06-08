package andiag.coru.es.welegends;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Canalejas on 06/06/2016.
 */
public class Utils {

    public static final int MAX_LEVEL = 30;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }

    public static String toTitleCase(String s) {
        final String ACTIONABLE_DELIMITERS = " '-/";
        StringBuilder sb = new StringBuilder();
        boolean capNext = true;
        for (char c : s.toCharArray()) {
            c = (capNext) ? Character.toUpperCase(c) : Character.toLowerCase(c);
            sb.append(c);
            capNext = (ACTIONABLE_DELIMITERS.indexOf((int) c) >= 0);
        }
        return sb.toString();
    }

    public static String cleanString(String string) {
        return string.toLowerCase().replaceAll(" ", "").replace("\n", "").replace("\r", "");
    }

}
