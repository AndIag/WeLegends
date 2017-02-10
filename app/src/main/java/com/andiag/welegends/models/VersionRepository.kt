package com.andiag.welegends.models

import android.content.Context
import android.util.Log
import com.andiag.commons.interfaces.presenters.AIInterfaceErrorHandlerPresenter
import com.andiag.commons.interfaces.presenters.AIInterfaceLoaderHandlerPresenter
import com.andiag.welegends.R
import com.andiag.welegends.WeLegendsDatabase
import com.andiag.welegends.models.api.RestClient
import com.andiag.welegends.models.database.static_data.*
import com.andiag.welegends.models.utils.CallbackSemaphore
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
class VersionRepository private constructor() : IVersionRepository {
    private val TAG = VersionRepository::class.java.simpleName

    private val FILE_NAME = "VersionData"
    private val ARG_VERSION = "version"
    private var version: String? = null

    private var caller: AIInterfaceLoaderHandlerPresenter<String>? = null
    private var isVersionLoading: Boolean = false

    companion object {
        private var REPOSITORY: IVersionRepository? = null

        fun getInstance(): IVersionRepository {
            if (REPOSITORY == null) {
                REPOSITORY = VersionRepository()
            }
            return REPOSITORY!!
        }

    }

    /**
     * Set new version to local properties
     * @param [newVersion] version to save
     * @param [context] required to access sharedPreferences
     */
    private fun setVersion(newVersion: String, context: Context) {
        version = newVersion
        context.getSharedPreferences(FILE_NAME, 0).edit().clear().putString(ARG_VERSION, version).apply()

        if (caller != null) {
            caller!!.onLoadSuccess(version)
        }
        isVersionLoading = false
    }

    /**
     * Get local saved version
     * @param [context] required to access sharedPreferences
     */
    private fun getSavedVersion(context: Context): String {
        if (version == null) {
            val settings = context.getSharedPreferences(FILE_NAME, 0)
            version = settings.getString(ARG_VERSION, "6.23.1")
        }

        return version!!
    }

    override fun getVersion(caller: AIInterfaceLoaderHandlerPresenter<String>?): String? {
        if (version == null && caller != null) {
            this.caller = caller
            if (isVersionLoading) {
                caller.onLoadProgressChange(R.string.loadStaticData)
            }
        }
        return version
    }

    override fun isLoading(): Boolean {
        return isVersionLoading
    }

    // TODO clean this method, too long
    override fun loadVersion(caller: AIInterfaceErrorHandlerPresenter) {
        isVersionLoading = true
        val call: Call<List<String>> = RestClient.getVersion().versions()
        call.enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    doAsync {
                        val newVersion: String = response.body()[0]
                        Log.i(TAG, "Server Version: %s".format(newVersion))
                        if (newVersion != getSavedVersion(caller.context)) {
                            val locale = Locale.getDefault().toString()

                            Log.i(TAG, "Updated Server Version To: %s".format(newVersion))
                            Log.i(TAG, "Mobile Locale: %s".format(locale))

                            //Init semaphore with number of methods to load and callback method
                            val semaphore: CallbackSemaphore = CallbackSemaphore(5, Callable {
                                uiThread {
                                    Log.i(TAG, "CallbackSemaphore: StaticData Load Ended")
                                }
                            })

                            WeLegendsDatabase.recreateStaticTables(caller.context)

                            semaphore.acquire(5)
                            uiThread {
                                // Update version field to show loading feedback

                                //Load static data. !IMPORTANT change semaphore if some method change
                                Champion.loadFromServer(caller, semaphore, newVersion, locale)
                                Item.loadFromServer(caller, semaphore, newVersion, locale)
                                SummonerSpell.loadFromServer(caller, semaphore, newVersion, locale)
                                Mastery.loadFromServer(caller, semaphore, newVersion, locale)
                                Rune.loadFromServer(caller, semaphore, newVersion, locale)
                            }
                        }
                        uiThread {
                            setVersion(newVersion, caller.context) //Comment this line to test static data load
                            Log.i(TAG, "CallbackSemaphore: StaticData Load Ended")
                        }
                    }
                    return
                }
                isVersionLoading = false
                caller.onLoadError(response.message())
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Log.e(TAG, "ERROR: checkServerVersion - onFailure: %s".format(t.message))
                isVersionLoading = false
                caller.onLoadError(t.message)
            }
        })
    }

}
