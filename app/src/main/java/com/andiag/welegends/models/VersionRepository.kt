package com.andiag.welegends.models

import android.content.Context
import android.util.Log
import com.andiag.welegends.common.utils.CallbackData
import com.andiag.welegends.models.api.RestClient
import com.andiag.welegends.models.database.static_data.*
import com.andiag.welegends.models.database.static_data.utils.CallbackSemaphore
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
class VersionRepository private constructor(context: Context) : IVersionRepository {
    private val TAG = VersionRepository::class.java.simpleName

    private val FILE_NAME = "VersionData"
    private val ARG_VERSION = "version"

    private var mVersion: String? = null
    private var mLoading: Boolean = false
    private var mContext: Context = context

    companion object {
        private var REPOSITORY: IVersionRepository? = null

        fun getInstance(context: Context): IVersionRepository {
            if (REPOSITORY == null) {
                REPOSITORY = VersionRepository(context)
            }
            return REPOSITORY!!
        }

    }

    override fun save(newVersion: String) {
        save(newVersion, mContext)
    }

    override fun save(newVersion: String, context: Context) {
        mVersion = newVersion
        context.getSharedPreferences(FILE_NAME, 0).edit().clear().putString(ARG_VERSION, mVersion).apply()
        mLoading = false
    }

    override fun isLoading(): Boolean {
        return mLoading
    }

    override fun syncRead(): String? {
        return mVersion
    }

    override fun read(mode: LoadingType, callback: CallbackData<String>) {
        when (mode) {
            LoadingType.NO_LOAD -> {
                callback.onSuccess(mVersion)
            }
            LoadingType.LOCAL_LOAD -> {
                mLoading = true
                doAsync {
                    mVersion = onLocalLoad()
                    if (mVersion != null) {
                        uiThread {
                            mLoading = false
                            callback.onSuccess(mVersion)
                        }
                    } else {
                        uiThread {
                            mLoading = false
                            callback.onError(Throwable("Unable to load local version"))
                        }
                    }
                }
            }
            LoadingType.REMOTE_LOAD -> {
                mLoading = true
                onRemoteLoad(callback)
            }
        }
    }

    private fun onLocalLoad(): String? {
        val settings = mContext.getSharedPreferences(FILE_NAME, 0)
        return settings.getString(ARG_VERSION, "6.23.1")
    }

    private fun onRemoteLoad(callback: CallbackData<String>) {
        val call: Call<List<String>> = RestClient.getVersion().versions()
        call.enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    doAsync {
                        val newVersion: String = response.body()[0]
                        Log.i(TAG, "Server Version: %s".format(newVersion))
                        if (newVersion != onLocalLoad()) {
                            uiThread {
                                loadStatics(newVersion, callback)
                            }
                        }
                        uiThread {
                            save(newVersion) //Comment this line to test static data load
                            callback.onSuccess(newVersion)
                            Log.i(TAG, "CallbackSemaphore: StaticData Load Ended")
                        }
                    }
                    return
                }
                callback.onError(Throwable(response.message()))
                mLoading = false
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Log.e(TAG, "ERROR: checkServerVersion - onFailure: %s".format(t.message))
                callback.onError(t)
                mLoading = false
            }
        })
    }

    private fun loadStatics(newVersion: String, callback: CallbackData<String>) {
        val locale = Locale.getDefault().toString()

        Log.i(TAG, "Updated Server Version To: %s".format(newVersion))
        Log.i(TAG, "Mobile Locale: %s".format(locale))

        //Init semaphore with number of methods to load and callback method
        val semaphore: CallbackSemaphore = CallbackSemaphore(5, Callable {
            Log.i(TAG, "CallbackSemaphore: StaticData Load Ended")
        })
        semaphore.acquire(5)

        //Load static data. !IMPORTANT change semaphore if some method change
        Champion.loadFromServer(callback, semaphore, newVersion, locale)
        Item.loadFromServer(callback, semaphore, newVersion, locale)
        SummonerSpell.loadFromServer(callback, semaphore, newVersion, locale)
        Mastery.loadFromServer(callback, semaphore, newVersion, locale)
        Rune.loadFromServer(callback, semaphore, newVersion, locale)
    }

}
