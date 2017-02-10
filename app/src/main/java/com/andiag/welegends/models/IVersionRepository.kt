package com.andiag.welegends.models

import android.content.Context
import retrofit2.Callback

/**
 * Created by Canalejas on 10/02/2017.
 */
interface IVersionRepository {
    /**
     * Get loaded version
     */
    fun getVersion(): String?

    /**
     * Ask if app is loading static data
     * @return [Boolean] true if loading false otherwise
     */
    fun isLoading(): Boolean

    /**
     * Check if our local version correspond to server version. If not, call all static data loaders
     * @return [AIInterfaceLoaderPresenter.onLoadSuccess] or [AIInterfaceLoaderPresenter.onLoadError]
     */
    fun loadVersion(callback: Callback<List<String>>)

    fun setVersion(newVersion: String, context: Context)
    fun getSavedVersion(context: Context): String
}