package es.coru.andiag.welegends.find_summoner

import android.util.Log
import com.raizlabs.android.dbflow.config.FlowManager
import es.coru.andiag.andiag_mvp.base.BaseFragmentPresenter
import es.coru.andiag.welegends.R
import es.coru.andiag.welegends.WeLegendsDatabase
import es.coru.andiag.welegends.common.utils.CallbackSemaphore
import es.coru.andiag.welegends.common.utils.StringUtils
import es.coru.andiag.welegends.find_summoner.implementation.ActivityFindSummoner
import es.coru.andiag.welegends.find_summoner.implementation.FragmentFindSummoner
import es.coru.andiag.welegends.models.Version
import es.coru.andiag.welegends.models.database.Champion
import es.coru.andiag.welegends.models.database.ProfileIcon
import es.coru.andiag.welegends.models.database.Summoner
import es.coru.andiag.welegends.models.dto.GenericStaticData
import es.coru.andiag.welegends.models.rest.RestClient
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.Callable


/**
 * Created by andyq on 09/12/2016.
 */
class PresenterFragmentFindSummoner : BaseFragmentPresenter<FragmentFindSummoner, ActivityFindSummoner>() {

    private var semaphore: CallbackSemaphore? = null

    override fun onViewAttached() {
        checkServerVersion()
    }

    override fun onViewDetached() {

    }

    //region FindSummoner
    fun getSummonerByName(name: String, region: String) {
        Log.d(TAG, "searchSummonerByName")
        val cleanName = StringUtils.cleanString(name)
        if (!cleanName.isEmpty()) {
            val dbSummoner: Summoner? = null
//            var dbSummoner: Summoner? = SQLite.select().from<Summoner>(Summoner::class.java)
//                    .where(Summoner_Table.name.eq(name))
//                    .and(Summoner_Table.region.eq(region))
//                    .querySingle()
            if (dbSummoner == null) {
                val call = RestClient.getWeLegendsData(cleanName).getSummonerByName(region, cleanName)
                call.enqueue(object : Callback<Summoner> {
                    override fun onResponse(call: Call<Summoner>, response: Response<Summoner>) {
                        doAsync {
                            val summoner: Summoner = response.body()
                            summoner.region = region
                            summoner.save()
                            uiThread {
                                view.onSummonerFound(summoner)
                            }
                        }
                    }

                    override fun onFailure(call: Call<Summoner>, t: Throwable) {
                        view!!.onSummonerNotFound(R.string.error404)
                    }
                })
            } else {
                view!!.onSummonerFound(dbSummoner)
            }
        } else {
            view!!.onSummonerNotFound(R.string.voidSummonerError)
        }
    }
    //endregion

    private fun recreateDatabase() {
        Log.i(TAG, "Recreating Database 4new Version")
        FlowManager.getDatabase(WeLegendsDatabase.NAME).reset(parent!!.applicationContext)
    }

    //region Data Loaders
    private fun checkServerVersion() {
        parent!!.showLoading()
        val call: Call<List<String>> = RestClient.getWeLegendsData().getServerVersion()
        call.enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    doAsync {
                        val newVersion: String = response.body()[0]
                        Log.i(TAG, "Server Version: %s".format(newVersion))
                        if (newVersion != Version.getVersion(parent!!)) {
                            //Version.setVersion(newVersion, getView() as Context)
                            val locale = Locale.getDefault().toString()

                            Log.i(TAG, "Updated Server Version To: %s".format(newVersion))
                            Log.i(TAG, "Mobile Locale: %s".format(locale))

                            //Init semaphore with number of methods to load and callback method
                            semaphore = CallbackSemaphore(2, Callable {
                                uiThread {
                                    view!!.onVersionUpdate(newVersion)
                                    parent!!.hideLoading()
                                    Log.i(TAG, "Data Load Ended")
                                }
                            })

//                            recreateDatabase()

                            semaphore!!.acquire(2)
                            uiThread {
                                // Update version field to show loading feedback
                                view!!.onVersionUpdate(view!!.getString(R.string.loadStaticData))

                                //Load static data. !IMPORTANT change semaphore if some method change
                                loadServerChampions(version = newVersion, locale = locale)
                                loadProfileIcons(version = newVersion, locale = locale)
                                //TODO load other info
                            }
                        }

                    }
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Log.e(TAG, "ERROR: checkServerVersion - onFailure: %s".format(t.message))
                parent!!.hideLoading()
            }
        })
    }

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
                    semaphore!!.release(1)
                    parent!!.errorLoading(null)
                } else {
                    doAsync {
                        try {
                            Log.i(TAG, "Loaded Champions: %s".format(response.body().data!!.keys))
                            FlowManager.getDatabase(Champion::class.java).writableDatabase
                            FlowManager.getModelAdapter(Champion::class.java).saveAll(response.body().data!!.values)
                        } catch (e: Exception) {
                            Log.e(TAG, "Error updating champions: %s".format(e.message))
                            e.printStackTrace()
                            uiThread {
                                parent!!.errorLoading(null)
                            }
                        } finally {
                            Log.i(TAG, "Semaphore released for: loadServerChampions")
                            semaphore!!.release(1)
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
                semaphore!!.release(1)
                parent!!.errorLoading(null)
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
                    semaphore!!.release(1)
                    parent!!.errorLoading(null)
                } else {
                    doAsync {
                        try {
                            Log.i(TAG, "Loaded ProfileIcons: %s".format(response.body().data!!.keys))
                            FlowManager.getModelAdapter(ProfileIcon::class.java).saveAll(response.body().data!!.values)
                        } catch (e: Exception) {
                            Log.e(TAG, "Error updating profile icons: %s".format(e.message))
                            //e.printStackTrace()
                            uiThread {
                                parent!!.errorLoading(null)
                            }
                        } finally {
                            Log.i(TAG, "Semaphore released for: loadProfileIcons")
                            semaphore!!.release(1)
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
                semaphore!!.release(1)
                parent!!.errorLoading(null)
            }
        })
    }
    //endregion

    companion object {
        internal val TAG = PresenterFragmentFindSummoner::class.java.simpleName
    }
}