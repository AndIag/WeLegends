package es.coru.andiag.welegends.find

import android.content.Context
import android.util.Log
import es.coru.andiag.welegends.models.Version
import es.coru.andiag.welegends.models.database.DBSummoner
import es.coru.andiag.welegends.models.entities.Champion
import es.coru.andiag.welegends.models.entities.dto.GenericStaticData
import es.coru.andiag.welegends.models.rest.RestClient
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


/**
 * Created by Canalejas on 08/12/2016.
 */
class InitPresenter private constructor() {

    private val TAG = InitPresenter::class.java.simpleName

    private var view: InitView? = null
    private var database: DBSummoner? = null

    fun onViewAttached(view: InitView) {
        this.view = view
        if (database == null) {
            database = DBSummoner.getInstance(view as Context)
        }

        checkServerVersion()

    }

    fun onViewDetached() {
        this.view = null
    }

    private fun checkServerVersion() {
        (view as InitActivity).showLoading()
        val call: Call<List<String>> = RestClient.getWeLegendsData().getServerVersion()
        call.enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    doAsync {
                        val newVersion: String = response.body()[0]
                        Log.i(TAG, "Server Version: %s".format(newVersion))
                        if (newVersion != Version.getVersion(view as Context)) {
                            Version.setVersion(newVersion, view as Context)
                            val locale = Locale.getDefault().toString()

                            Log.i(TAG, "Updated Server Version To: %s".format(newVersion))
                            Log.i(TAG, "Mobile Locale: %s".format(locale))

                            uiThread {
                                (view as InitView).updateVersion(newVersion)
                                loadServerChampions(version = newVersion, locale = locale)
                                //TODO load other info
                            }

                        } else {
                            uiThread {
                                (view as InitView).hideLoading()
                            }
                        }

                    }
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Log.e(TAG, "ERROR: checkServerVersion - onFailure: %s".format(t.message))
                (view as InitActivity).hideLoading()
            }
        })
    }

    private fun loadServerChampions(version: String, locale: String) {
        val call = RestClient.getDdragonStaticData(version, locale).champions()
        call.enqueue(object : Callback<GenericStaticData<String, Champion>> {
            override fun onResponse(call: Call<GenericStaticData<String, Champion>>, response: Response<GenericStaticData<String, Champion>>) {
                if (response.isSuccessful) {
                    doAsync {
                        if (response.body() == null) {
                            Log.e(TAG, "ERROR: loadServerChampions - onResponse: %s".format(response.errorBody().string()))
                            if (locale != RestClient.DEFAULT_LOCALE) {
                                Log.i(TAG, "Reloading Locale From onResponse To: %s".format(RestClient.DEFAULT_LOCALE))

                                uiThread {
                                    loadServerChampions(version, RestClient.DEFAULT_LOCALE)
                                }
                            }
                        }
                        Log.i(TAG, "Loaded Champions: %s".format(response.body().data))
                        //TODO save in database
                    }
                }
            }

            override fun onFailure(call: Call<GenericStaticData<String, Champion>>, t: Throwable) {
                Log.e(TAG, "ERROR: loadServerChampions - onFailure: %s".format(t.message))
                if (locale != RestClient.DEFAULT_LOCALE) {
                    Log.i(TAG, "Reloading Locale From onFailure To: %s".format(RestClient.DEFAULT_LOCALE))
                    loadServerChampions(version, RestClient.DEFAULT_LOCALE)
                }
                //TODO handle error
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
