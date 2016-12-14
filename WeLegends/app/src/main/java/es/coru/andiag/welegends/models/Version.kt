package es.coru.andiag.welegends.models

import android.content.Context
import android.util.Log
import es.coru.andiag.welegends.R
import es.coru.andiag.welegends.WeLegendsDatabase
import es.coru.andiag.welegends.models.api.RestClient
import es.coru.andiag.welegends.models.database.static_data.*
import es.coru.andiag.welegends.models.utils.CallbackSemaphore
import es.coru.andiag.welegends.presenters.summoners.DataLoader
import es.coru.andiag.welegends.presenters.summoners.PresenterFragmentFindSummoner
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.Callable

/**
 * Created by Canalejas on 08/12/2016.
 */
object Version {

    private val FILE_NAME = "VersionData"
    private val ARG_VERSION = "Version"
    private var version: String? = null

    private fun setVersion(newVersion: String, context: Context) {
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

    fun checkServerVersion(caller: DataLoader) {
        val call: Call<List<String>> = RestClient.getWeLegendsData().getServerVersion()
        call.enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    doAsync {
                        val newVersion: String = response.body()[0]
                        Log.i(PresenterFragmentFindSummoner.TAG, "Server Version: %s".format(newVersion))
                        if (newVersion != getVersion(caller.getContext())) {
                            setVersion(newVersion, caller.getContext()) //Comment this line to test static data load
                            val locale = Locale.getDefault().toString()

                            Log.i(PresenterFragmentFindSummoner.TAG, "Updated Server Version To: %s".format(newVersion))
                            Log.i(PresenterFragmentFindSummoner.TAG, "Mobile Locale: %s".format(locale))

                            //Init semaphore with number of methods to load and callback method
                            val semaphore: CallbackSemaphore = CallbackSemaphore(6, Callable {
                                uiThread {
                                    caller.onLoadSuccess(newVersion)
                                    Log.i(PresenterFragmentFindSummoner.TAG, "CallbackSemaphore: StaticData Load Ended")
                                }
                            })

                            WeLegendsDatabase.recreateDatabase(caller.getContext())

                            semaphore.acquire(6)
                            uiThread {
                                // Update version field to show loading feedback
                                caller.onLoadProgressChange(caller.getContext().getString(R.string.loadStaticData))

                                //Load static data. !IMPORTANT change semaphore if some method change
                                Champion.loadFromServer(caller, semaphore, newVersion, locale)
                                ProfileIcon.loadFromServer(caller, semaphore, newVersion, locale)
                                Item.loadFromServer(caller, semaphore, newVersion, locale)
                                SummonerSpell.loadFromServer(caller, semaphore, newVersion, locale)
                                Mastery.loadFromServer(caller, semaphore, newVersion, locale)
                                Rune.loadFromServer(caller, semaphore, newVersion, locale)
                            }
                        }

                    }
                }
                caller.onLoadError(null)
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Log.e(PresenterFragmentFindSummoner.TAG, "ERROR: checkServerVersion - onFailure: %s".format(t.message))
                caller.onLoadError(null)
            }
        })
    }

}
