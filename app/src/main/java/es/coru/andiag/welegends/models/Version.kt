package es.coru.andiag.welegends.models

import android.content.Context
import android.util.Log
import es.coru.andiag.andiag_mvp.presenters.AIInterfaceLoaderPresenter
import es.coru.andiag.welegends.R
import es.coru.andiag.welegends.WeLegendsDatabase
import es.coru.andiag.welegends.models.utils.CallbackSemaphore
import es.coru.andiag.welegends.models.wrapped.api.RestClient
import es.coru.andiag.welegends.models.wrapped.database.static_data.*
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
    private val TAG = Version::class.java.simpleName
    
    private val FILE_NAME = "VersionData"
    private val ARG_VERSION = "Version"
    private var version: String? = null

    /**
     * Set new version to local properties
     * @param [newVersion] version to save
     * @param [context] required to access sharedPreferences
     */
    private fun setVersion(newVersion: String, context: Context) {
        version = newVersion

        val settings = context.getSharedPreferences(FILE_NAME, 0)
        settings.edit().clear()
                .putString(ARG_VERSION, version)
                .apply()
    }

    /**
     * Get local saved version
     * @param [context] required to access sharedPreferences
     */
    fun getVersion(context: Context): String {
        if (version == null) {
            val settings = context.getSharedPreferences(FILE_NAME, 0)
            version = settings.getString(ARG_VERSION, "6.23.1")
        }

        return version!!
    }

    /**
     * Check if our local version correspond to server version. If not, call all static data loaders
     * @return [AIInterfaceLoaderPresenter.onLoadSuccess] or [AIInterfaceLoaderPresenter.onLoadError]
     */
    fun checkServerVersion(caller: AIInterfaceLoaderPresenter<String>) {
        val call: Call<List<String>> = RestClient.getVersion().versions()
        call.enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    doAsync {
                        val newVersion: String = response.body()[0]
                        Log.i(TAG, "Server Version: %s".format(newVersion))
                        if (newVersion != getVersion(caller.context)) {
                            setVersion(newVersion, caller.context) //Comment this line to test static data load
                            val locale = Locale.getDefault().toString()

                            Log.i(TAG, "Updated Server Version To: %s".format(newVersion))
                            Log.i(TAG, "Mobile Locale: %s".format(locale))

                            //Init semaphore with number of methods to load and callback method
                            val semaphore: CallbackSemaphore = CallbackSemaphore(5, Callable {
                                uiThread {
                                    caller.onLoadSuccess(newVersion)
                                    Log.i(TAG, "CallbackSemaphore: StaticData Load Ended")
                                }
                            })

                            WeLegendsDatabase.recreateStaticTables(caller.context)

                            semaphore.acquire(5)
                            uiThread {
                                // Update version field to show loading feedback
                                caller.onLoadProgressChange(caller.context.getString(R.string.loadStaticData))

                                //Load static data. !IMPORTANT change semaphore if some method change
                                Champion.loadFromServer(caller, semaphore, newVersion, locale)
                                Item.loadFromServer(caller, semaphore, newVersion, locale)
                                SummonerSpell.loadFromServer(caller, semaphore, newVersion, locale)
                                Mastery.loadFromServer(caller, semaphore, newVersion, locale)
                                Rune.loadFromServer(caller, semaphore, newVersion, locale)
                            }
                        }
                        uiThread {
                            caller.onLoadSuccess(newVersion)
                            Log.i(TAG, "CallbackSemaphore: StaticData Load Ended")
                        }
                    }
                    return
                }
                caller.onLoadError(response.message())
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Log.e(TAG, "ERROR: checkServerVersion - onFailure: %s".format(t.message))
                caller.onLoadError(t.message)
            }
        })
    }

}