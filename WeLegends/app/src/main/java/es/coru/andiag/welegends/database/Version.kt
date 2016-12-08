package es.coru.andiag.welegends.database

import android.app.Activity
import android.content.SharedPreferences

/**
 * Created by Canalejas on 08/12/2016.
 */

object Version {

    private val FILE_NAME = "VersionData"
    private val ARG_VERSION = "Version"
    private var version: String? = null

    fun setVersion(newVersion: String, activity: Activity) {
        version = newVersion

        val settings = activity.getSharedPreferences(FILE_NAME, 0)
        settings.edit().clear()
                .putString(ARG_VERSION, version)
                .apply()
    }

    fun getVersion(activity: Activity): String {
        if (version == null) {
            val settings = activity.getSharedPreferences(FILE_NAME, 0)
            version = settings.getString(ARG_VERSION, "6.11.1")
        }

        return version!!
    }

}
