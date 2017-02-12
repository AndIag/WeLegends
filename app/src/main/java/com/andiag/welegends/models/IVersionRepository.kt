package com.andiag.welegends.models

import android.content.Context
import com.andiag.welegends.common.utils.CallbackData

/**
 * Created by Canalejas on 10/02/2017.
 */
interface IVersionRepository {

    /**
     * Load version matching mode and use callback or the response
     * @param [mode] loading mode
     * @param [callback] called on error or success
     */
    fun read(mode: LoadingType, callback: CallbackData<String>)

    /**
     * Return loaded version or null
     */
    fun syncRead(): String?

    /**
     * Set new version to local properties
     * @param [newVersion] version to save
     * @param [context] required to access sharedPreferences
     */
    fun save(newVersion: String, context: Context)

    /**
     * Set new version to local properties
     * @param [newVersion] version to save
     */
    fun save(newVersion: String)

    /**
     * Ask if app is loading static data
     * @return [Boolean] true if loading false otherwise
     */
    fun isLoading(): Boolean

}