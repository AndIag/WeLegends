package com.andiag.welegends.models

import android.content.Context
import com.andiag.commons.interfaces.presenters.AIInterfaceLoaderHandlerPresenter
import com.andiag.welegends.models.api.RestClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Canalejas on 08/12/2016.
 */
class VersionRepository private constructor() : IVersionRepository {
    private val TAG = VersionRepository::class.java.simpleName

    private val FILE_NAME = "VersionData"
    private val ARG_VERSION = "version"
    private var version: String? = null

    private var caller: AIInterfaceLoaderHandlerPresenter<String>? = null
    private var staticDataLoading: Boolean = false

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
     * Get local saved version
     * @param [context] required to access sharedPreferences
     */
    override fun getSavedVersion(context: Context): String {
        if (version == null) {
            val settings = context.getSharedPreferences(FILE_NAME, 0)
            version = settings.getString(ARG_VERSION, "6.23.1")
        }

        return version!!
    }

    /**
     * Set new version to local properties
     * @param [newVersion] version to save
     * @param [context] required to access sharedPreferences
     */
    override fun setVersion(newVersion: String, context: Context) {
        version = newVersion
        context.getSharedPreferences(FILE_NAME, 0).edit().clear().putString(ARG_VERSION, version).apply()

        if (caller != null) {
            caller!!.onLoadSuccess(version)
        }
        staticDataLoading = false
    }

    override fun getVersion(): String? {
        return version
    }

    override fun isLoading(): Boolean {
        return staticDataLoading
    }

    override fun loadVersion(callback: Callback<List<String>>) {
        staticDataLoading = true
        val call: Call<List<String>> = RestClient.getVersion().versions()
        call.enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>?, response: Response<List<String>>?) {
                callback.onResponse(call, response)
                staticDataLoading = false
            }

            override fun onFailure(call: Call<List<String>>?, t: Throwable?) {
                callback.onFailure(call, t)
                staticDataLoading = false
            }

        })
    }

}
