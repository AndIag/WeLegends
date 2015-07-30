package andiag.coru.es.welegends.utils;

import org.apache.http.HttpStatus;

import andiag.coru.es.welegends.R;

/**
 * Created by Iago on 30/07/2015.
 */
public abstract class NetworkError {

    public static int parseServerError(int statusCode) {
        int message;
        switch (statusCode) {
            case HttpStatus.SC_INTERNAL_SERVER_ERROR:
                message = R.string.error500;
                break;
            case HttpStatus.SC_SERVICE_UNAVAILABLE:
                message = R.string.error503;
                break;
            case HttpStatus.SC_NOT_FOUND:
                message = R.string.error404;
                break;
            default:
                message = R.string.errorDefault;
                break;
        }
        return message;
    }

}
