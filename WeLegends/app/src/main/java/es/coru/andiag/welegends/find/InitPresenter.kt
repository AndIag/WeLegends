package es.coru.andiag.welegends.find

import android.app.Activity
import es.coru.andiag.welegends.database.DBSummoner
import es.coru.andiag.welegends.rest.RestClient
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Call
import android.icu.util.ULocale.getLanguage
import android.icu.util.ULocale.getCountry
import android.util.Log
import es.coru.andiag.welegends.database.Version
import retrofit2.Callback
import retrofit2.Response
import android.icu.util.ULocale.getLanguage
import android.icu.util.ULocale.getCountry
import java.util.*


/**
 * Created by Canalejas on 08/12/2016.
 */

class InitPresenter private constructor() {

    private val TAG = "InitPresenter"

    private var activityInit: ActivityInit? = null
    private var db: DBSummoner? = null

    fun onViewAttached(activityInit: ActivityInit) {
        this.activityInit = activityInit
        if (db == null){
            db = DBSummoner.getInstance(activityInit)
        }

        checkServerVersion()

    }

    fun onViewDetached() {
        this.activityInit = null
    }

    private fun checkServerVersion() {
        (activityInit as ActivityInit).showLoading()
        val call: Call<List<String>> = RestClient.getWeLegendsData().getServerVersion()
        call.enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    doAsync {
                        val newVersion: String = response.body()[0]
                        Log.i(TAG, "BACKGROUND: checkServerVersion -> FOUND: %s".format(newVersion))
                        if(newVersion != Version.getVersion(activityInit!!)){
                            Version.setVersion(newVersion, activityInit as Activity)
                            val locale = Locale.getDefault().toString()

                            Log.i(TAG, "BACKGROUND: checkServerVersion -> NEW VERSION: %s".format(newVersion))
                            Log.i(TAG, "BACKGROUND: checkServerVersion -> LOCALE: %s".format(locale))

                            uiThread {
                                (activityInit as ActivityInit).hideLoading()
                            }

                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Log.e(TAG, "BACKGROUND: checkServerVersion -> FAIL")
                (activityInit as ActivityInit).hideLoading()
            }
        })
    }

    companion object {

        private var presenter: InitPresenter? = null

        val instance: InitPresenter
            get() {
                if (presenter == null) {
                    presenter = InitPresenter()
                }
                return presenter!!
            }
    }

}
