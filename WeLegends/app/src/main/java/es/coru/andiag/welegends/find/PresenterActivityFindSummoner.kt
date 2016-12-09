package es.coru.andiag.welegends.find

import android.content.Context
import android.util.Log
import com.orm.SchemaGenerator
import com.orm.SugarContext
import com.orm.SugarDb
import com.orm.SugarRecord
import es.coru.andiag.welegends.R
import es.coru.andiag.welegends.common.BasePresenter
import es.coru.andiag.welegends.models.Version
import es.coru.andiag.welegends.models.entities.Champion
import es.coru.andiag.welegends.models.entities.ProfileIcon
import es.coru.andiag.welegends.models.entities.dto.GenericStaticData
import es.coru.andiag.welegends.models.rest.RestClient
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.Semaphore


/**
 * Created by Canalejas on 08/12/2016.
 */
class PresenterActivityFindSummoner() : BasePresenter<ViewActivityFindSummoner>() {

    private val TAG = es.coru.andiag.welegends.find.PresenterActivityFindSummoner::class.java.simpleName

    private var semaphore: Semaphore? = null

    override fun onAttach() {
        super.onAttach()
        checkServerVersion()
    }

    private fun recreateDatabase() {
        Log.i(TAG, "Recreating Database 4new Version")
        SugarContext.terminate()
        val schemaGenerator = SchemaGenerator((getView() as Context).applicationContext)
        schemaGenerator.deleteTables(SugarDb((getView() as Context).applicationContext).db)
        SugarContext.init((getView() as Context).applicationContext)
        schemaGenerator.createDatabase(SugarDb((getView() as Context).applicationContext).db)
    }

    private fun checkServerVersion() {
        (getView() as ActivityFindSummoner).showLoading()
        val call: Call<List<String>> = RestClient.getWeLegendsData().getServerVersion()
        call.enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    //Init semaphore with number of methods to load
                    semaphore = Semaphore(2)
                    doAsync {
                        val newVersion: String = response.body()[0]
                        Log.i(TAG, "Server Version: %s".format(newVersion))
                        if (newVersion != Version.getVersion(getView() as Context)) {
                            //If our version was older we need to reload data

                            //TODO uncomment this line
                            //Version.setVersion(newVersion, getView() as Context)
                            val locale = Locale.getDefault().toString()

                            Log.i(TAG, "Updated Server Version To: %s".format(newVersion))
                            Log.i(TAG, "Mobile Locale: %s".format(locale))

                            recreateDatabase()

                            semaphore!!.acquire(2)
                            uiThread { // Update version field to show loading feedback
                                (getView() as ViewActivityFindSummoner).updateVersion((getView() as Context)
                                        .getString(R.string.loadStaticData))

                                //Load static data. !IMPORTANT change semaphore if some method change
                                loadServerChampions(version = newVersion, locale = locale)
                                loadProfileIcons(version = newVersion, locale = locale)
                                //TODO load other info
                            }
                        }

                        semaphore!!.acquire()
                        Log.i(TAG, "Semaphore acquired for: checkServerVersion")
                        uiThread {
                            (getView() as ViewActivityFindSummoner).updateVersion(newVersion)
                            (getView() as ViewActivityFindSummoner).hideLoading()
                        }
                        Log.i(TAG, "Semaphore released for: checkServerVersion")
                        semaphore!!.release()

                    }
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Log.e(TAG, "ERROR: checkServerVersion - onFailure: %s".format(t.message))
                (getView() as ActivityFindSummoner).hideLoading()
            }
        })
    }

    //region Data Loaders
    private fun loadServerChampions(version: String, locale: String) {
        val call = RestClient.getDdragonStaticData(version, locale).champions()
        call.enqueue(object : Callback<GenericStaticData<String, Champion>> {
            override fun onResponse(call: Call<GenericStaticData<String, Champion>>, response: Response<GenericStaticData<String, Champion>>) {
                if (!response.isSuccessful || response.body() == null) {
                    if (locale != RestClient.DEFAULT_LOCALE) {
                        Log.i(TAG, "Reloading loadServerChampions Locale From onResponse To: %s".format(RestClient.DEFAULT_LOCALE))
                        loadServerChampions(version, RestClient.DEFAULT_LOCALE)
                        return
                    }
                    Log.e(TAG, "ERROR: loadServerChampions - onResponse: %s".format(response.errorBody().string()))
                    Log.i(TAG, "Semaphore released with error for: loadServerChampions")
                    semaphore!!.release()
                    (getView() as ViewActivityFindSummoner).errorLoading(null)
                } else {
                    doAsync {
                        try {
                            Log.i(TAG, "Loaded Champions: %s".format(response.body().data!!.keys))
                            SugarRecord.deleteAll(Champion().javaClass)
                            SugarRecord.saveInTx(response.body().data!!.values)
                        } catch (e: Exception) {
                            Log.e(TAG, "Error updating champions: %s".format(e.message))
                            uiThread {
                                (getView() as ViewActivityFindSummoner).errorLoading(null)
                            }
                        } finally {
                            Log.i(TAG, "Semaphore released for: loadServerChampions")
                            semaphore!!.release()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<GenericStaticData<String, Champion>>, t: Throwable) {
                if (locale != RestClient.DEFAULT_LOCALE) {
                    Log.i(TAG, "Reloading loadServerChampions Locale From onFailure To: %s".format(RestClient.DEFAULT_LOCALE))
                    loadServerChampions(version, RestClient.DEFAULT_LOCALE)
                    return
                }
                Log.e(TAG, "ERROR: loadServerChampions - onFailure: %s".format(t.message))
                Log.i(TAG, "Semaphore released with error for: loadServerChampions")
                semaphore!!.release()
                (getView() as ViewActivityFindSummoner).errorLoading(null)
            }
        })
    }

    private fun loadProfileIcons(version: String, locale: String) {
        val call = RestClient.getDdragonStaticData(version, locale).profileIcons()
        call.enqueue(object : Callback<GenericStaticData<String, ProfileIcon>> {
            override fun onResponse(call: Call<GenericStaticData<String, ProfileIcon>>, response: Response<GenericStaticData<String, ProfileIcon>>) {
                if (!response.isSuccessful || response.body() == null) {
                    if (locale != RestClient.DEFAULT_LOCALE) {
                        Log.i(TAG, "Reloading loadProfileIcons Locale From onResponse To: %s".format(RestClient.DEFAULT_LOCALE))
                        loadProfileIcons(version, RestClient.DEFAULT_LOCALE)
                        return
                    }
                    Log.e(TAG, "ERROR: loadProfileIcons - onResponse: %s".format(response.errorBody().string()))
                    Log.i(TAG, "Semaphore released with error for: loadProfileIcons")
                    semaphore!!.release()
                    (getView() as ViewActivityFindSummoner).errorLoading(null)
                } else {
                    doAsync {
                        try {
                            Log.i(TAG, "Loaded ProfileIcons: %s".format(response.body().data!!.keys))
                            SugarRecord.deleteAll(ProfileIcon().javaClass)
                            SugarRecord.saveInTx(response.body().data!!.values)
                        } catch (e: Exception) {
                            Log.e(TAG, "Error updating profile icons: %s".format(e.message))
                            uiThread {
                                (getView() as ViewActivityFindSummoner).errorLoading(null)
                            }
                        } finally {
                            Log.i(TAG, "Semaphore released for: loadProfileIcons")
                            semaphore!!.release()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<GenericStaticData<String, ProfileIcon>>, t: Throwable) {
                if (locale != RestClient.DEFAULT_LOCALE) {
                    Log.i(TAG, "Reloading loadProfileIcons Locale From onFailure To: %s".format(RestClient.DEFAULT_LOCALE))
                    loadProfileIcons(version, RestClient.DEFAULT_LOCALE)
                    return
                }
                Log.e(TAG, "ERROR: loadProfileIcons - onFailure: %s".format(t.message))
                Log.i(TAG, "Semaphore released with error for: loadProfileIcons")
                semaphore!!.release()
                (getView() as ViewActivityFindSummoner).errorLoading(null)
            }
        })
    }
    //endregion

    companion object {

        private var presenter: es.coru.andiag.welegends.find.PresenterActivityFindSummoner? = null

        val instance: es.coru.andiag.welegends.find.PresenterActivityFindSummoner
            get() {
                if (presenter == null) {
                    presenter = PresenterActivityFindSummoner()
                }
                return presenter!!
            }

    }
}