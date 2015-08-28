package andiag.coru.es.welegends.utils.handlers;

import com.android.volley.NetworkResponse;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.apache.http.HttpStatus;

import andiag.coru.es.welegends.R;

/**
 * Created by Iago on 30/07/2015.
 */
public abstract class MyNetworkError {

    private static NetworkResponse networkResponse;

    public static int parseVolleyError(VolleyError error) {
        int message = R.string.unknowkError;
        if (error.networkResponse == null) {
            if (error.getClass().equals(TimeoutError.class)) {
                message = R.string.timeout;
            }
        } else {
            networkResponse = error.networkResponse;

            switch (networkResponse.statusCode) {
                case HttpStatus.SC_INTERNAL_SERVER_ERROR:
                    message = R.string.error500;
                    break;
                case HttpStatus.SC_SERVICE_UNAVAILABLE:
                    message = R.string.error500;
                    break;
                case HttpStatus.SC_NOT_FOUND:
                    message = R.string.error404;
                    break;
                default:
                    message = R.string.unknowkError;
                    break;
            }

        }
        return message;
    }

}
