package es.coru.andiag.welegends.find_summoner

import android.util.Log
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.structure.InvalidDBConfiguration
import es.coru.andiag.andiag_mvp.base.BaseFragmentPresenter
import es.coru.andiag.welegends.R
import es.coru.andiag.welegends.WeLegendsDatabase
import es.coru.andiag.welegends.common.utils.CallbackSemaphore
import es.coru.andiag.welegends.common.utils.StringUtils
import es.coru.andiag.welegends.find_summoner.implementation.ActivityFindSummoner
import es.coru.andiag.welegends.find_summoner.implementation.FragmentFindSummoner
import es.coru.andiag.welegends.models.Version
import es.coru.andiag.welegends.models.database.Champion
import es.coru.andiag.welegends.models.database.Item
import es.coru.andiag.welegends.models.database.ProfileIcon
import es.coru.andiag.welegends.models.database.Summoner
import es.coru.andiag.welegends.models.rest.RestClient
import es.coru.andiag.welegends.models.rest.utils.StaticDataCallback
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
        try {
            Log.i(TAG, "Recreating Database 4new Version")
            FlowManager.getDatabase(WeLegendsDatabase.NAME).reset(parent!!.applicationContext)
        } catch (e: InvalidDBConfiguration) {
            Log.i(TAG, "Database did not exist")
        }
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
                            semaphore = CallbackSemaphore(3, Callable {
                                uiThread {
                                    view!!.onVersionUpdate(newVersion)
                                    parent!!.hideLoading()
                                    Log.i(TAG, "CallbackSemaphore: StaticData Load Ended")
                                }
                            })

                            recreateDatabase()

                            semaphore!!.acquire(3)
                            uiThread {
                                // Update version field to show loading feedback
                                view!!.onVersionUpdate(view!!.getString(R.string.loadStaticData))

                                //Load static data. !IMPORTANT change semaphore if some method change
                                loadServerChampions(version = newVersion, locale = locale)
                                loadProfileIcons(version = newVersion, locale = locale)
                                loadItems(version = newVersion, locale = locale)
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
        call.enqueue(StaticDataCallback(semaphore!!, locale, parent, Champion::class.java,
                Runnable {
                    Log.i(TAG, "Reloading %s Locale From onResponse To: %s".format(Champion::class.java.simpleName, RestClient.DEFAULT_LOCALE))
                    loadServerChampions(version, RestClient.DEFAULT_LOCALE)
                }))
    }

    private fun loadProfileIcons(version: String, locale: String) {
        val call = RestClient.getDdragonStaticData(version, locale).profileIcons()
        call.enqueue(StaticDataCallback(semaphore!!, locale, parent, ProfileIcon::class.java,
                Runnable {
                    Log.i(TAG, "Reloading %s Locale From onResponse To: %s".format(ProfileIcon::class.java.simpleName, RestClient.DEFAULT_LOCALE))
                    loadProfileIcons(version, RestClient.DEFAULT_LOCALE)
                }))
    }

    private fun loadItems(version: String, locale: String) {
        Log.d(TAG, "Loading items")
        val call = RestClient.getDdragonStaticData(version, locale).items()
        call.enqueue(StaticDataCallback(semaphore!!, locale, parent, Item::class.java,
                Runnable {
                    Log.i(TAG, "Reloading %s Locale From onResponse To: %s".format(Item::class.java.simpleName, RestClient.DEFAULT_LOCALE))
                    loadItems(version, RestClient.DEFAULT_LOCALE)
                }))
    }

    //endregion

    companion object {
        internal val TAG = PresenterFragmentFindSummoner::class.java.simpleName
    }
}