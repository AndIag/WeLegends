package es.coru.andiag.welegends.common.utils

/**
 * Created by andyq on 09/12/2016.
 */
object StringUtils {

    fun toTitleCase(s: String): String {
        val ACTIONABLE_DELIMITERS = " '-/"
        val sb = StringBuilder()
        var capNext = true
        for (c in s.toCharArray()) {
            val ns = if (capNext) Character.toUpperCase(c) else Character.toLowerCase(c)
            sb.append(ns)
            capNext = ACTIONABLE_DELIMITERS.contains(ns)
        }
        return sb.toString()
    }

    fun cleanString(string: String): String {
        return string.toLowerCase().replace(" ".toRegex(), "").replace("\n", "").replace("\r", "")
    }

}