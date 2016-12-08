package es.coru.andiag.welegends.common.database

import android.content.Context

/**
 * Created by Canalejas on 08/12/2016.
 */

object Version {

    private val FILE_NAME = "VersionData"
    private val ARG_VERSION = "Version"
    private var version: String? = null

    fun setVersion(newVersion: String, context: Context) {
        version = newVersion

        val settings = context.getSharedPreferences(FILE_NAME, 0)
        settings.edit().clear()
                .putString(ARG_VERSION, version)
                .apply()
    }

    fun getVersion(context: Context): String {
        if (version == null) {
            val settings = context.getSharedPreferences(FILE_NAME, 0)
            version = settings.getString(ARG_VERSION, "6.23.1")
        }

        return version!!
    }

}
