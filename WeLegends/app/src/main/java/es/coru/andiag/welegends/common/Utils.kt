package es.coru.andiag.welegends.common

import android.content.Context
import android.net.ConnectivityManager

/**
 * Created by andyq on 09/12/2016.
 */
object Utils {

    val MAX_LEVEL = 30
    val DEFAULT_LOCALE = "en_GB"
    var isServerLoaded = false

    fun isNetworkAvailable(context: Context): Boolean {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo

        var isAvailable = false
        if (networkInfo != null && networkInfo.isConnected) {
            isAvailable = true
        }

        return isAvailable
    }

//    fun toTitleCase(s: String): String {
//        val ACTIONABLE_DELIMITERS = " '-/"
//        val sb = StringBuilder()
//        var capNext = true
//        for (c in s.toCharArray()) {
//            c = if (capNext) Character.toUpperCase(c) else Character.toLowerCase(c)
//            sb.append(c)
//            capNext = ACTIONABLE_DELIMITERS.indexOf(c.toInt()) >= 0
//        }
//        return sb.toString()
//    }

    fun cleanString(string: String): String {
        return string.toLowerCase().replace(" ".toRegex(), "").replace("\n", "").replace("\r", "")
    }

}